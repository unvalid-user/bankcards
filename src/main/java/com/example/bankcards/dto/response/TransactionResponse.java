package com.example.bankcards.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public record TransactionResponse (
        Long id,

        Long userId,

        Long sourceCardId,

        Long destinationCardId,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
        BigDecimal monetaryAmount
) {}
