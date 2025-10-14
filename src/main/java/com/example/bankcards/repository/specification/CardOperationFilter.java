package com.example.bankcards.repository.specification;

import com.example.bankcards.entity.card_operation.CardOperationStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CardOperationFilter {
    private Long id;
    private Long userId;
    private CardOperationStatus status;
}
