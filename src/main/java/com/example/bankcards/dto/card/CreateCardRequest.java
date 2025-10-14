package com.example.bankcards.dto.card;

import com.example.bankcards.validation.annotation.ValidCardNumber;
import com.example.bankcards.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

import static com.example.bankcards.validation.ValidationMessage.EXPIRATION_DATE_MUST_BE_FUTURE;

public record CreateCardRequest(
        @NotNull
        @ValidCardNumber
        String cardNumber,

        @NotNull
        @ValidPhoneNumber
        String ownerPhoneNumber,

        @NotNull
        @Future(message = EXPIRATION_DATE_MUST_BE_FUTURE)
        LocalDate expirationDate
) {}
