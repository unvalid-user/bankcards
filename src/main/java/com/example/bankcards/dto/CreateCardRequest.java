package com.example.bankcards.dto;

import java.time.LocalDate;

public record CreateCardRequest(
    String cardNumber,
    String ownerPhoneNumber,
    LocalDate expirationDate
) {}
