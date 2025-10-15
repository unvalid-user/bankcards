package com.example.bankcards.dto.mapper;

import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.dto.PagedResponse;
import com.example.bankcards.dto.card_operation.BlockCardOperationResponse;
import com.example.bankcards.dto.card_operation.CardOperationResponse;
import com.example.bankcards.entity.card_operation.BlockCardOperation;
import com.example.bankcards.entity.card_operation.CardOperation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(config = MapStructConfig.class)
public interface CardOperationMapper {
    BlockCardOperationResponse toResponse(BlockCardOperation op);

    default CardOperationResponse toResponse(CardOperation op) {
        if (op instanceof BlockCardOperation block) {
            return toResponse(block);
        }
        throw new IllegalArgumentException("Unsupported type: " + op.getClass());
    }

    PagedResponse<CardOperationResponse> toPagedResponse(Page<CardOperation> pageOps);
}
