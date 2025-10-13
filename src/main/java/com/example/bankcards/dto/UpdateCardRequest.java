package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;

import java.time.LocalDate;

public record UpdateCardRequest (
        LocalDate expirationDate,
        CardStatus status
) {}