package com.accounting.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = XLSXValidator.class)
@Documented
public @interface ValidXLSX {
    String message() default "Invalid XLSX File";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
