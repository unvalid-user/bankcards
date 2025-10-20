package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        String phoneNumber,

        @NotNull
        String password
) {}
