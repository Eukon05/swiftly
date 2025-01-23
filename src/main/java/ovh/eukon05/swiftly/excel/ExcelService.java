package ovh.eukon05.swiftly.excel;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.stereotype.Component;
import ovh.eukon05.swiftly.exception.BankAlreadyExistsException;
import ovh.eukon05.swiftly.service.BankService;
import ovh.eukon05.swiftly.web.dto.BankDTO;

import java.io.*;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExcelService {
    private final BankService bankService;

    private static final String INVALID_ROW = "Invalid row found at {}, skipping...";
    private static final String DUPLICATE_ROW = "Duplicate row found at {}, skipping...";

    private static final int ISO2_COLUMN = 0;
    private static final int SWIFT_COLUMN = 1;
    private static final int NAME_COLUMN = 3;
    private static final int ADDRESS_COLUMN = 4;
    private static final int COUNTRY_COLUMN = 6;

    public void parseExcel(String fileName) {
        try(InputStream is = new BufferedInputStream(new FileInputStream(fileName)); ReadableWorkbook excel = new ReadableWorkbook(is)){
            Sheet sh = excel.getFirstSheet();
            sh.openStream().skip(1).forEach(r -> {
                Optional<String> iso2;
                Optional<String> swift;
                Optional<String> name;
                Optional<String> address;
                Optional<String> country;

                iso2 = r.getCellAsString(ISO2_COLUMN);
                swift = r.getCellAsString(SWIFT_COLUMN);
                name = r.getCellAsString(NAME_COLUMN);
                address = r.getCellAsString(ADDRESS_COLUMN);
                country = r.getCellAsString(COUNTRY_COLUMN);

                if(iso2.isEmpty() || swift.isEmpty() || name.isEmpty() || address.isEmpty() ||country.isEmpty()){
                    log.warn(INVALID_ROW, r.getRowNum());
                    return;
                }

                try {
                    BankDTO bank = new BankDTO(swift.get(), name.get(), address.get(), iso2.get(), country.get());
                    bankService.createBank(bank);
                }
                catch(IllegalArgumentException | ConstraintViolationException e){
                    log.warn(INVALID_ROW, r.getRowNum());
                }
                catch(BankAlreadyExistsException e){
                    log.warn(DUPLICATE_ROW, r.getRowNum());
                }
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
