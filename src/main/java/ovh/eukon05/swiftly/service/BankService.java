package ovh.eukon05.swiftly.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ovh.eukon05.swiftly.database.BankEntity;
import ovh.eukon05.swiftly.database.BankRepository;
import ovh.eukon05.swiftly.exception.BankAlreadyExistsException;
import ovh.eukon05.swiftly.exception.BankNotFoundException;
import ovh.eukon05.swiftly.web.dto.BankDTO;
import ovh.eukon05.swiftly.web.dto.CountryDTO;
import ovh.eukon05.swiftly.web.dto.HeadquarterDTO;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BankService {
    private final BankRepository bankRepository;
    private static final Function<BankEntity, BankDTO> TO_BANK_DTO = entity -> new BankDTO(entity.getSwiftCode(), entity.getBankName(), entity.getAddress(), entity.getCountryISO2(), entity.getCountryName());

    public BankDTO getBank(String swiftCode) {
        BankEntity entity = bankRepository.findById(swiftCode).orElseThrow(BankNotFoundException::new);

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

    public void deleteBank(String swiftCode) {
        BankEntity entity = bankRepository.findById(swiftCode).orElseThrow(BankNotFoundException::new);
        bankRepository.delete(entity);
    }

    public void createBank(BankDTO bankDTO) {
        if(bankRepository.existsById(bankDTO.getSwiftCode()))
            throw new BankAlreadyExistsException();

        BankEntity entity = new BankEntity(bankDTO.getSwiftCode(), bankDTO.getBankName(), bankDTO.getAddress(), bankDTO.getCountryISO2(), bankDTO.getCountryName());
        bankRepository.save(entity);
    }

    public CountryDTO getBanksByCountry(String countryISO2){
        List<BankDTO> branches = bankRepository.findAllByCountryISO2(countryISO2)
                .stream()
                .map(TO_BANK_DTO)
                .toList();

        return new CountryDTO(countryISO2, branches);
    }

}
