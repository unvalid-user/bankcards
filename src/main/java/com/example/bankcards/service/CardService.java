package com.example.bankcards.service;

import com.example.bankcards.dto.request.CreateCardRequest;
import com.example.bankcards.dto.request.UpdateCardRequest;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.exception.ConflictException;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.dto.filter.CardFilter;
import com.example.bankcards.repository.specification.CardSpecifications;
import com.example.bankcards.util.Encryptor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.example.bankcards.util.AppConst.*;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserService userService;
    private final CardMapper cardMapper;
    private final Encryptor encryptor;


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

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteCardById(Long cardId) {
        Card card = findCardById(cardId);
        if (card.getBalance().compareTo(BigDecimal.ZERO) != 0)
            throw new ConflictException("Cannot delete card with positive balance");

        cardRepository.delete(card);

        if (cardRepository.existsById(cardId))
            throw new IllegalStateException("Card was not deleted for some reason");
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Card updateCard(Long cardId, UpdateCardRequest updateCardRequest) {
        Card card = findCardById(cardId);
        cardMapper.updateCardFromDto(updateCardRequest, card);

        return cardRepository.save(card);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transferMoney(Long sourceCardId, Long destinationCardId, BigDecimal monetaryAmount, Long userId) {
        if (sourceCardId == destinationCardId) {
            throw new BadRequestException("Cannot transfer money to the same card");
        }

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
