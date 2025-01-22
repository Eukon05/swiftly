package ovh.eukon05.swiftly.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Locale;

public class ISO2CodeValidator implements ConstraintValidator<ISO2Code, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null || s.length() != 2 || s.isBlank()) return false;
        return Arrays.asList(Locale.getISOCountries()).contains(s);
    }
}
