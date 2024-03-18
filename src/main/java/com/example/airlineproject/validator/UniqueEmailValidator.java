package com.example.airlineproject.validator;

import com.example.airlineproject.annotation.UniqueEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements
        ConstraintValidator<UniqueEmail, String> {
    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        // here must be email validation logic {AA}
        return false;
    }
}
