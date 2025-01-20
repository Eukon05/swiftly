package ovh.eukon05.swiftly.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ovh.eukon05.swiftly.service.BankService;
import ovh.eukon05.swiftly.web.dto.BankDTO;

@RestController
@RequestMapping("/api/v1/swift-codes")
@AllArgsConstructor
public class BankController {
    private final BankService bankService;

    @GetMapping("/{swift}")
    public BankDTO getBank(@PathVariable("swift") String swift) {
        return bankService.getBank(swift);
    }

    @DeleteMapping("/{swift}")
    public void deleteBank(@PathVariable("swift") String swift) {
        bankService.deleteBank(swift);
    }

    @PostMapping
    public void createBank(@Valid @RequestBody BankDTO bank) {
        bankService.createBank(bank);
    }
}
