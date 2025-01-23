package ovh.eukon05.swiftly.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ovh.eukon05.swiftly.annotation.ISO2Code;
import ovh.eukon05.swiftly.service.BankService;
import ovh.eukon05.swiftly.web.dto.BankDTO;
import ovh.eukon05.swiftly.web.dto.CountryDTO;
import ovh.eukon05.swiftly.web.dto.ResultDTO;

import static ovh.eukon05.swiftly.util.Message.SUCCESS;

@RestController
@RequestMapping("/api/v1/swift-codes")
@RequiredArgsConstructor
@Validated
public class BankController {
    private static final ResultDTO SUCCESS_DTO = new ResultDTO(SUCCESS);
    private final BankService bankService;

    @GetMapping("/{swift}")
    public BankDTO getBank(@PathVariable("swift") String swift) {
        return bankService.getBank(swift);
    }

    @DeleteMapping("/{swift}")
    public ResultDTO deleteBank(@PathVariable("swift") String swift) {
        bankService.deleteBank(swift);
        return SUCCESS_DTO;
    }

    @PostMapping
    public ResultDTO createBank(@Valid @RequestBody BankDTO bank) {
        bankService.createBank(bank);
        return SUCCESS_DTO;
    }

    @GetMapping("/country/{countryISO2}")
    public CountryDTO getBanksByCountry(@PathVariable("countryISO2") @Valid @ISO2Code String countryISO2) {
        return bankService.getBanksByCountry(countryISO2);
    }
}
