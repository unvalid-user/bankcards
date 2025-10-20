package com.example.bankcards.dto.mapper;

import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.dto.response.PagedResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.request.UpdateCardRequest;
import com.example.bankcards.entity.Card;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(config = MapStructConfig.class)
public interface CardMapper {
    @Mapping(
            target = "maskedNumber",
            expression = "java(maskCardNumber(card.getMaskedNumber()))"
    )
    CardResponse toResponse(Card card);

    @Mapping(target = "page", source = "number")
    PagedResponse<CardResponse> toPagedResponse(Page<Card> pageCards);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCardFromDto(UpdateCardRequest dto, @MappingTarget Card card);

    default String maskCardNumber(String num) {
        return "**** **** **** " + num;
    }
}
