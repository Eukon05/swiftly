package ovh.eukon05.swiftly.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ovh.eukon05.swiftly.util.Message.INVALID_ISO2_FORMAT;

@Constraint(validatedBy = ISO2CodeValidator.class)
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ISO2Code {
    String message() default INVALID_ISO2_FORMAT;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
