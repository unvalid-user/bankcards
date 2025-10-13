package com.example.bankcards.dto.mapper;

import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.UpdateCardRequest;
import com.example.bankcards.entity.Card;
import org.mapstruct.*;

@Mapper(config = MapStructConfig.class)
public interface CardMapper {
    @Mapping(
            target = "maskedNumber",
            expression = "java(maskCardNumber(card.getMaskedNumber()))"
    )
    CardResponse toCardResponse(Card card);

    // TODO: test
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCardFromDto(UpdateCardRequest dto, @MappingTarget Card card);

    default String maskCardNumber(String num) {
        return "**** **** **** " + num;
    }
}
