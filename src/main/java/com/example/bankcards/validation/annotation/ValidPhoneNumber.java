package com.example.bankcards.validation.annotation;

import com.example.bankcards.validation.validator.CardNumberValidator;
import com.example.bankcards.validation.validator.PhoneNumberValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.example.bankcards.validation.ValidationMessage.PHONE_NUMBER_INVALID_FORMAT;


@Constraint(validatedBy = PhoneNumberValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default PHONE_NUMBER_INVALID_FORMAT;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
