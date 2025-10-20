package com.example.bankcards.dto.request;

import com.example.bankcards.entity.CardStatus;
import jakarta.validation.constraints.Future;

import java.time.LocalDate;

import static com.example.bankcards.validation.ValidationMessage.EXPIRATION_DATE_MUST_BE_FUTURE;

public record UpdateCardRequest (
        @Future(message = EXPIRATION_DATE_MUST_BE_FUTURE)
        LocalDate expirationDate,

        CardStatus status
) {}