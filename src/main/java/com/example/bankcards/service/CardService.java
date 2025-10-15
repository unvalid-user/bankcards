package com.example.bankcards.service;

import com.example.bankcards.dto.card.CreateCardRequest;
import com.example.bankcards.dto.card.UpdateCardRequest;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.exception.BadRequestException;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.example.bankcards.util.AppConst.*;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private Encryptor encryptor;


    public Card findCardByIdAndUser(Long cardId, Long userId) {
        return cardRepository.findByIdAndUserId(cardId, userId).orElseThrow(() ->
                new ResourceNotFoundException(CARD, ID, cardId));
    }
    public Card getCardById(Long cardId, Long userId) {
        return findCardByIdAndUser(cardId, userId);
    }
    public Card findCardById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() ->
                new ResourceNotFoundException(CARD, ID, cardId));
    }

    // TODO:
    //  userIdOrPhoneNumber?
    public Card createCard(CreateCardRequest createCardRequest) {
        Card card = Card.builder()
                // TODO: check if card number already exists?
                .number(encryptor.encrypt(createCardRequest.cardNumber()))
                .maskedNumber(createCardRequest.cardNumber().substring(12, 16))
                .userId(userService.findUserByPhoneNumber(createCardRequest.ownerPhoneNumber()).getId())
                .expirationDate(createCardRequest.expirationDate())
                .build();

        return cardRepository.save(card);
    }

    public Page<Card> findCardsWithSpecification(Pageable pageable, CardFilter filter) {
        Specification<Card> spec = CardSpecifications.withFilter(filter);
        return cardRepository.findAll(spec, pageable);
    }

    public Page<Card> getCardsWithFilter(Pageable pageable, CardFilter filter) {
        return findCardsWithSpecification(pageable, filter);
    }

    public Page<Card> getCardsByUserId(Pageable pageable, CardFilter filter, Long userId) {
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

    public Card updateCard(Long cardId, UpdateCardRequest updateCardRequest) {
        Card card = findCardById(cardId);
        cardMapper.updateCardFromDto(updateCardRequest, card);

        return cardRepository.save(card);
    }

    @Transactional
    public void transferMoney(Long sourceCardId, Long destinationCardId, BigDecimal monetaryAmount, Long userId) {
        Card sourceCard = findCardByIdAndUser(sourceCardId, userId);
        Card destinationCard = findCardByIdAndUser(destinationCardId, userId);

        if (sourceCard.getBalance().compareTo(monetaryAmount) < 0)
            throw new BadRequestException("Insufficient balance");

        cardStatusShouldBeActive(sourceCard);
        cardStatusShouldBeActive(destinationCard);

        sourceCard.setBalance(sourceCard.getBalance().subtract(monetaryAmount));
        destinationCard.setBalance(destinationCard.getBalance().add(monetaryAmount));

        cardRepository.save(sourceCard);
        cardRepository.save(destinationCard);
    }

    private void cardStatusShouldBeActive(Card card) {
        if (card.getStatus() != CardStatus.ACTIVE)
            throw new BadRequestException(String.format("Card *%s is %s",
                    card.getMaskedNumber(), card.getStatus()));
    }
}
