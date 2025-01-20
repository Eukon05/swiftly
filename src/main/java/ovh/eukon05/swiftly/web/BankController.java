package ovh.eukon05.swiftly.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
