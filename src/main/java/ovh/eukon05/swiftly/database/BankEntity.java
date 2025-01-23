package ovh.eukon05.swiftly.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import ovh.eukon05.swiftly.annotation.ISO2Code;

import java.util.Objects;

import static ovh.eukon05.swiftly.util.Message.*;

@Entity
@Table(name = "banks")
@NoArgsConstructor
@Data
public class BankEntity {
    @Id
    @Size(min = 8, max = 11)
    @NotBlank(message = INVALID_SWIFT_BLANK)
    private String swiftCode;

    @NotBlank(message = INVALID_NAME)
    private String bankName;
    private String address;

    @ISO2Code
    private String countryISO2;

    @NotBlank(message = INVALID_COUNTRY)
    private String countryName;

    private boolean headquarter;

    public BankEntity(String swiftCode, String bankName, String address, String countryISO2, String countryName) {
        Objects.requireNonNull(swiftCode, INVALID_SWIFT_BLANK);
        Objects.requireNonNull(bankName, INVALID_NAME);
        Objects.requireNonNull(countryISO2, INVALID_ISO2_BLANK);
        Objects.requireNonNull(countryName, INVALID_COUNTRY);

        if(countryISO2.length() != 2)
            throw new IllegalArgumentException(INVALID_ISO2_FORMAT);

        this.swiftCode = swiftCode;
        headquarter = swiftCode.endsWith("XXX");
        this.bankName = bankName;
        this.address = address == null ? "" : address;
        this.countryISO2 = countryISO2.toUpperCase();
        this.countryName = countryName.toUpperCase();
    }

    //Overrides for default lombok implementations of the setters to include always storing the values in uppercase

    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2.toUpperCase();
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName.toUpperCase();
    }
}
