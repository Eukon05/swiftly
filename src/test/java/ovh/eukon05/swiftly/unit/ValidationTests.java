package ovh.eukon05.swiftly.unit;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import ovh.eukon05.swiftly.web.dto.BankDTO;
import ovh.eukon05.swiftly.database.BankEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationTests {
    private static final String VALID_SWIFT_CODE = "12345678XXX";
    private static final String VALID_BANK_NAME = "TestBank";
    private static final String VALID_ADDRESS = "Test Street 31, Warsaw";
    private static final String VALID_COUNTRY_ISO2 = "PL";
    private static final String VALID_COUNTRY_NAME = "POLAND";

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void should_throw_exception_when_swift_code_is_null() {
        assertThrows(NullPointerException.class, () -> new BankDTO(null, VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
        assertThrows(NullPointerException.class, () -> new BankEntity(null, VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
    }

    @Test
    void should_throw_exception_when_swift_code_is_empty() {
        assertThrows(IllegalArgumentException.class, () -> new BankDTO("", VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
        assertThrows(IllegalArgumentException.class, () -> new BankEntity("", VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
    }

    @Test
    void should_throw_exception_when_swift_code_is_too_short() {
        assertThrows(IllegalArgumentException.class, () -> new BankDTO("1234567", VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
        assertThrows(IllegalArgumentException.class, () -> new BankEntity("1234567", VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
    }

    @Test
    void should_throw_exception_when_swift_code_is_too_long() {
        assertThrows(IllegalArgumentException.class, () -> new BankDTO("123456789012", VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
        assertThrows(IllegalArgumentException.class, () -> new BankEntity("123456789012", VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
    }

    @Test
    void should_throw_exception_when_bank_name_is_null() {
        assertThrows(NullPointerException.class, () -> new BankDTO(VALID_SWIFT_CODE, null, VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
        assertThrows(NullPointerException.class, () -> new BankEntity(VALID_SWIFT_CODE, null, VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
    }

    @Test
    void should_throw_exception_when_bank_name_is_empty() {
        Set<ConstraintViolation<BankDTO>> v1 = validator.validate(new BankDTO(VALID_SWIFT_CODE, "", VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
        Set<ConstraintViolation<BankDTO>> v2 = validator.validate(new BankDTO(VALID_SWIFT_CODE, "", VALID_ADDRESS, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));

        assertEquals(1, v1.size());
        assertEquals(1, v2.size());
    }

    @Test
    void should_throw_exception_when_address_is_null() {
        assertThrows(NullPointerException.class, () -> new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, null, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
        assertThrows(NullPointerException.class, () -> new BankEntity(VALID_SWIFT_CODE, VALID_BANK_NAME, null, VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
    }

    @Test
    void should_throw_exception_when_address_is_empty() {
        Set<ConstraintViolation<BankDTO>> v1 = validator.validate(new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, "", VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));
        Set<ConstraintViolation<BankDTO>> v2 = validator.validate(new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, "", VALID_COUNTRY_ISO2, VALID_COUNTRY_NAME));

        assertEquals(1, v1.size());
        assertEquals(1, v2.size());
    }

    @Test
    void should_throw_exception_when_country_iso2_is_null() {
        assertThrows(NullPointerException.class, () -> new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, null, VALID_COUNTRY_NAME));
        assertThrows(NullPointerException.class, () -> new BankEntity(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, null, VALID_COUNTRY_NAME));
    }

    @Test
    void should_throw_exception_when_country_iso2_is_empty() {
        assertThrows(IllegalArgumentException.class, () -> new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, "", VALID_COUNTRY_NAME));
        assertThrows(IllegalArgumentException.class, () -> new BankEntity(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, "", VALID_COUNTRY_NAME));
    }

    @Test
    void should_throw_exception_when_country_iso2_is_too_short() {
        assertThrows(IllegalArgumentException.class, () -> new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, "P", VALID_COUNTRY_NAME));
        assertThrows(IllegalArgumentException.class, () -> new BankEntity(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, "P", VALID_COUNTRY_NAME));
    }

    @Test
    void should_throw_exception_when_country_iso2_is_too_long() {
        assertThrows(IllegalArgumentException.class, () -> new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, "POL", VALID_COUNTRY_NAME));
        assertThrows(IllegalArgumentException.class, () -> new BankEntity(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, "POL", VALID_COUNTRY_NAME));
    }

    @Test
    void should_throw_exception_when_country_name_is_null() {
        assertThrows(NullPointerException.class, () -> new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, null));
        assertThrows(NullPointerException.class, () -> new BankEntity(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, null));
    }

    @Test
    void should_throw_exception_when_country_name_is_empty() {
        Set<ConstraintViolation<BankDTO>> v1 = validator.validate(new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, ""));
        Set<ConstraintViolation<BankDTO>> v2 = validator.validate(new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, VALID_COUNTRY_ISO2, ""));

        assertEquals(1, v1.size());
        assertEquals(1, v2.size());
    }

    @Test
    void should_throw_exception_when_iso_does_not_exist(){
        Set<ConstraintViolation<BankDTO>> v1 = validator.validate(new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, "XX", VALID_COUNTRY_NAME));
        Set<ConstraintViolation<BankDTO>> v2 = validator.validate(new BankDTO(VALID_SWIFT_CODE, VALID_BANK_NAME, VALID_ADDRESS, "XX", VALID_COUNTRY_NAME));

        assertEquals(1, v1.size());
        assertEquals(1, v2.size());
    }
}