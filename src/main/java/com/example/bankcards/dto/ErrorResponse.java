package com.example.bankcards.dto;

import lombok.Builder;

@Builder
public record ErrorResponse (
        String error,
        String message
) {}
