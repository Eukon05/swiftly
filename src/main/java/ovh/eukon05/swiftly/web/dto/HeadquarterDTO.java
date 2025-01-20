package ovh.eukon05.swiftly.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class HeadquarterDTO extends BankDTO{
    @NotNull
    private final List<BankDTO> branches;

    public HeadquarterDTO(String swiftCode, String bankName, String address, String countryISO2, String countryName, List<BankDTO> branches) {
        super(swiftCode, bankName, address, countryISO2, countryName);

        Objects.requireNonNull(branches);
        this.branches = branches;
    }
}
