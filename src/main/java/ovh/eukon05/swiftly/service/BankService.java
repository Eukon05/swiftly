package ovh.eukon05.swiftly.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ovh.eukon05.swiftly.annotation.ISO2Code;
import ovh.eukon05.swiftly.database.BankEntity;
import ovh.eukon05.swiftly.database.BankRepository;
import ovh.eukon05.swiftly.exception.BankAlreadyExistsException;
import ovh.eukon05.swiftly.exception.BankNotFoundException;
import ovh.eukon05.swiftly.exception.DeleteDataMismatchException;
import ovh.eukon05.swiftly.web.dto.BankDTO;
import ovh.eukon05.swiftly.web.dto.CountryDTO;
import ovh.eukon05.swiftly.web.dto.DeleteRequestDTO;
import ovh.eukon05.swiftly.web.dto.HeadquarterDTO;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@Service
@Validated
@RequiredArgsConstructor
public class BankService {
    private final BankRepository bankRepository;
    private static final Function<BankEntity, BankDTO> TO_BANK_DTO = entity -> new BankDTO(entity.getSwiftCode(), entity.getBankName(), entity.getAddress(), entity.getCountryISO2(), entity.getCountryName());

    public BankDTO getBank(@NotBlank String swiftCode) {
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

    public void deleteBank(@NotBlank String swiftCode, @Valid DeleteRequestDTO dto) {
        BankEntity entity = bankRepository.findById(swiftCode).orElseThrow(BankNotFoundException::new);

        if(!entity.getBankName().equals(dto.bankName()) || !entity.getCountryISO2().equals(dto.countryISO2()))
            throw new DeleteDataMismatchException();

        bankRepository.delete(entity);
    }

    public void createBank(@Valid BankDTO bankDTO) {
        if(bankRepository.existsById(bankDTO.getSwiftCode()))
            throw new BankAlreadyExistsException();

        BankEntity entity = new BankEntity(bankDTO.getSwiftCode(), bankDTO.getBankName(), bankDTO.getAddress(), bankDTO.getCountryISO2(), bankDTO.getCountryName());
        bankRepository.save(entity);
    }

    public CountryDTO getBanksByCountry(@Valid @ISO2Code String countryISO2){
        List<BankDTO> branches = bankRepository.findAllByCountryISO2(countryISO2)
                .stream()
                .map(TO_BANK_DTO)
                .toList();

        Locale locale = Locale.of("", countryISO2);
        return new CountryDTO(countryISO2, locale.getDisplayCountry(Locale.ENGLISH).toUpperCase(), branches);
    }

}
