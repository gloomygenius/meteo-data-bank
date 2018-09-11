package com.mdbank.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LongitudeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LongitudeConstraint {
    String message() default "Invalid longitude";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
