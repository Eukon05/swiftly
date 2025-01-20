package ovh.eukon05.swiftly.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ovh.eukon05.swiftly.database.BankEntity;
import ovh.eukon05.swiftly.database.BankRepository;
import ovh.eukon05.swiftly.web.dto.BankDTO;
import ovh.eukon05.swiftly.web.dto.HeadquarterDTO;

import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class BankService {
    private final BankRepository bankRepository;
    private static final Function<BankEntity, BankDTO> TO_BANK_DTO = entity -> new BankDTO(entity.getSwiftCode(), entity.getBankName(), entity.getAddress(), entity.getCountryISO2(), entity.getCountryName());

    public BankDTO getBank(String swiftCode) {
        BankEntity entity = bankRepository.findById(swiftCode).orElseThrow(() -> new RuntimeException("Bank not found"));

        if(entity.isHeadquarter()){
            List<BankDTO> branches = bankRepository
                    .findAllBySwiftCodeStartingWithAndHeadquarterFalse(swiftCode.substring(0, 8))
                    .stream()
                    .map(TO_BANK_DTO)
                    .toList();

            return new HeadquarterDTO(entity.getSwiftCode(), entity.getBankName(), entity.getAddress(), entity.getCountryISO2(), entity.getCountryName(), branches);
        }
        else
            return TO_BANK_DTO.apply(entity);
    }

}
