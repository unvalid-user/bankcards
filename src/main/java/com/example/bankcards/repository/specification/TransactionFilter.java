package com.example.bankcards.repository.specification;

import lombok.Data;

@Data
public class TransactionFilter {
    private Long id;
    private Long userId;
    private Long sourceCardId;
    private Long destinationCardId;
}
