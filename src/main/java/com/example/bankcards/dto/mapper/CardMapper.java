package com.example.bankcards.dto.mapper;

import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface CardMapper {
    @Mapping(
            target = "maskedNumber",
            expression = "java(maskCardNumber(card.getMaskedNumber()))"
    )
    CardResponse toCardResponse(Card card);

    default String maskCardNumber(String num) {
        return "**** **** **** " + num;
    }
}
