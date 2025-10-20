package com.example.bankcards.dto.mapper;

import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.dto.response.PagedResponse;
import com.example.bankcards.dto.response.BlockCardOperationResponse;
import com.example.bankcards.dto.response.CardOperationResponse;
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

    @Mapping(target = "page", source = "number")
    PagedResponse<CardOperationResponse> toPagedResponse(Page<CardOperation> pageOps);
}
