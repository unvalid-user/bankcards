package com.example.bankcards.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponse (
        String error,
        String message
) {}
