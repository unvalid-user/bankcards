package com.example.bankcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class CardOperationResponse {
    private Long id;
    private Long userId;
    private String status;
}
