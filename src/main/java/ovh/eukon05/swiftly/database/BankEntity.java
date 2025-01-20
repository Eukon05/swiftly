package ovh.eukon05.swiftly.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

import static ovh.eukon05.swiftly.util.Messages.*;

@Entity
@Table(name = "banks")
@NoArgsConstructor
@Data
public class BankEntity {
    @Id
    @NotBlank(message = INVALID_SWIFT)
    String swiftCode;

    @NotBlank(message = INVALID_NAME)
    String bankName;
    String address;

    @NotBlank(message = INVALID_ISO2_BLANK)
    @Size(min = 2, max = 2, message = INVALID_ISO2_FORMAT)
    String countryISO2;

    @NotBlank(message = INVALID_COUNTRY)
    String countryName;

    boolean headquarter;

    public BankEntity(String swiftCode, String bankName, String address, String countryISO2, String countryName) {
        Objects.requireNonNull(swiftCode, INVALID_SWIFT);
        Objects.requireNonNull(bankName, INVALID_NAME);
        Objects.requireNonNull(countryISO2, INVALID_ISO2_BLANK);
        Objects.requireNonNull(countryName, INVALID_COUNTRY);

        if(countryISO2.length() != 2)
            throw new IllegalArgumentException(INVALID_ISO2_FORMAT);

        this.swiftCode = swiftCode;
        headquarter = swiftCode.endsWith("XXX");
        this.bankName = bankName;
        this.address = address == null ? "" : address;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
    }
}
