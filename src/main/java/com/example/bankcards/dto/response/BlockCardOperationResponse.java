package com.example.bankcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockCardOperationResponse extends CardOperationResponse {
    private Long cardId;
}
