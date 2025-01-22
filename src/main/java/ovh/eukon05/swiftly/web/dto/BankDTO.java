package ovh.eukon05.swiftly.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import ovh.eukon05.swiftly.annotation.ISO2Code;

import java.util.Objects;

import static ovh.eukon05.swiftly.util.Message.*;

@Getter
public class BankDTO {
    @NotBlank(message = INVALID_SWIFT)
    @Size(min = 8, max = 11)
    private final String swiftCode;

    @NotBlank(message = INVALID_NAME)
    private final String bankName;

    private final String address;

    @ISO2Code
    private final String countryISO2;

    @NotBlank(message = INVALID_COUNTRY)
    private final String countryName;

    private final boolean isHeadquarter;

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
        this.countryISO2 = countryISO2.toUpperCase();
        this.countryName = countryName.toUpperCase();
    }

    // Override for the default lombok implementation to include the correct name for the JSON property
    @JsonProperty("isHeadquarter")
    public boolean isHeadquarter() {
        return isHeadquarter;
    }
}
