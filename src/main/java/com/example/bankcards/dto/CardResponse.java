package com.example.bankcards.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardResponse (
        Long id,

        String maskedNumber,

        Long userId,

        LocalDate expirationDate,

        String status,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
        BigDecimal balance
) {}
