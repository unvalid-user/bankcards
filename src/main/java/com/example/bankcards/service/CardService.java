package com.example.bankcards.service;

import com.example.bankcards.dto.card.CreateCardRequest;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.UpdateCardRequest;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.specification.CardFilter;
import com.example.bankcards.repository.specification.CardSpecifications;
import com.example.bankcards.util.Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.example.bankcards.util.AppConst.*;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private Encryptor encryptor;
    @Autowired
    private CardMapper cardMapper;

    public Card findCardByIdAndUser(Long cardId, Long userId) {
        return cardRepository.findByIdAndUserId(cardId, userId).orElseThrow(() ->
                new ResourceNotFoundException(CARD, ID, cardId));
    }
    public CardResponse getCardById(Long cardId, Long userId) {
        return cardMapper.toCardResponse(findCardByIdAndUser(cardId, userId));
    }
    public Card findCardById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() ->
                new ResourceNotFoundException(CARD, ID, cardId));
    }

    // TODO:
    //  userIdOrPhoneNumber?
    public CardResponse createCard(CreateCardRequest createCardRequest) {
        Card card = Card.builder()
                // TODO: check if card number already exists?
                .number(encryptor.encrypt(createCardRequest.cardNumber()))
                .maskedNumber(createCardRequest.cardNumber().substring(12, 16))
                .userId(userService.findUserByPhoneNumber(createCardRequest.ownerPhoneNumber()).getId())
                .expirationDate(createCardRequest.expirationDate())
                .build();

        return cardMapper.toCardResponse(cardRepository.save(card));
    }

    public Page<Card> findCardsWithSpecification(Pageable pageable, CardFilter filter) {
        Specification<Card> spec = CardSpecifications.withFilter(filter);
        return cardRepository.findAll(spec, pageable);
    }

    public Page<CardResponse> getCardsWithFilter(Pageable pageable, CardFilter filter) {
        return findCardsWithSpecification(pageable, filter)
                .map(cardMapper::toCardResponse);
    }

    public Page<CardResponse> getCardsByUserId(Pageable pageable, CardFilter filter, Long userId) {
        filter.setUserId(userId);

        return getCardsWithFilter(pageable, filter);
    }

    public void deleteCardById(Long cardId) {
        Card card = findCardById(cardId);
        // TODO: should server delete card with balance > 0?
        cardRepository.delete(card);

        if (cardRepository.existsById(cardId))
            throw new IllegalStateException("Card was not deleted for some reason");
    }

    public CardResponse updateCard(Long cardId, UpdateCardRequest updateCardRequest) {
        Card card = findCardById(cardId);
        cardMapper.updateCardFromDto(updateCardRequest, card);

        return cardMapper.toCardResponse(cardRepository.save(card));
    }
}
