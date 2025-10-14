package com.example.bankcards.dto.mapper;

import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.UpdateCardRequest;
import com.example.bankcards.entity.Card;
import org.mapstruct.*;

@Mapper(config = MapStructConfig.class)
public interface CardMapper {
    @Mapping(
            target = "maskedNumber",
            expression = "java(maskCardNumber(card.getMaskedNumber()))"
    )
    CardResponse toCardResponse(Card card);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCardFromDto(UpdateCardRequest dto, @MappingTarget Card card);

    default String maskCardNumber(String num) {
        return "**** **** **** " + num;
    }
}
