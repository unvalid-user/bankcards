package com.example.bankcards.validation.annotation;

import com.example.bankcards.validation.validator.CardNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.example.bankcards.validation.ValidationMessage.CARD_NUMBER_INVALID_FORMAT;


@Constraint(validatedBy = CardNumberValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCardNumber {
    String message() default CARD_NUMBER_INVALID_FORMAT;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
