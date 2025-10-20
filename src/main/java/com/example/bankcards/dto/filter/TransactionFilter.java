package com.example.bankcards.dto.filter;

import lombok.Data;

@Data
public class TransactionFilter {
    private Long id;
    private Long userId;
    private Long sourceCardId;
    private Long destinationCardId;
}
