package com.example.bankcards.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateTransactionRequest (
        @NotNull
        Long sourceCardId,

        @NotNull
        Long destinationCardId,

        @NotNull
        @Positive
        @Digits(integer = 10, fraction = 2)
        BigDecimal monetaryAmount
) {}
