package com.example.airlineproject.annotation;

import org.hibernate.validator.internal.constraintvalidators.hv.UniqueElementsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueElementsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
