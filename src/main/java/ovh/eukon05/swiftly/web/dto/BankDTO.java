package ovh.eukon05.swiftly.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Objects;

import static ovh.eukon05.swiftly.util.Messages.*;

@Getter
public class BankDTO {
    @NotBlank(message = INVALID_SWIFT)
    final String swiftCode;
    @NotBlank(message = INVALID_NAME)
    final String bankName;

    final String address;

    @NotBlank(message = INVALID_ISO2_BLANK)
    @Size(min = 2, max = 2, message = INVALID_ISO2_FORMAT)
    final String countryISO2;

    @NotBlank(message = INVALID_COUNTRY)
    String countryName;
    final boolean isHeadquarter;

    public BankDTO(String swiftCode, String bankName, String address, String countryISO2, String countryName) {
        Objects.requireNonNull(swiftCode, INVALID_SWIFT);
        Objects.requireNonNull(bankName, INVALID_NAME);
        Objects.requireNonNull(countryISO2, INVALID_ISO2_BLANK);
        Objects.requireNonNull(countryName, INVALID_COUNTRY);

        if(countryISO2.length() != 2)
            throw new IllegalArgumentException(INVALID_ISO2_FORMAT);

        this.swiftCode = swiftCode;
        isHeadquarter = swiftCode.endsWith("XXX");
        this.bankName = bankName;
        this.address = address == null ? "" : address;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
    }
}
