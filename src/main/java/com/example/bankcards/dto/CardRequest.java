package com.example.bankcards.dto;

import java.time.LocalDate;

public record CardRequest(
    String cardNumber,
    String ownerPhoneNumber,
    LocalDate expirationDate
) {}
