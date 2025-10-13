package com.example.bankcards.repository.specification;

import com.example.bankcards.entity.CardStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CardFilter {
    private Long id;
    private String maskedNumber;
    private Long userId;
    private CardStatus status;
}