package com.example.bankcards.repository.specification;

import com.example.bankcards.entity.CardStatus;
import lombok.Builder;

@Builder
public record CardFilter (
        Long id,
        String maskedNumber,
        Long userId,
        CardStatus status
) {}
