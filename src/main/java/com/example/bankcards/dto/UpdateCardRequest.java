package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import jakarta.validation.constraints.Future;

import java.time.LocalDate;

public record UpdateCardRequest (
        @Future(message = "Expiration Date must be in the future")
        LocalDate expirationDate,

        CardStatus status
) {}