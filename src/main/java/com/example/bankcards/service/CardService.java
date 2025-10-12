package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
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
    private UserRepository userRepository;
    @Autowired
    private Encryptor encryptor;
    @Autowired
    private CardMapper cardMapper;

    // TODO: 403 instead of 404?
    public Card getCardById(Long cardId, Long userId) {
        return cardRepository.findByIdAndUserId(cardId, userId).orElseThrow(() ->
                new ResourceNotFoundException(CARD, ID, cardId));
    }

    // TODO: userIdOrPhoneNumber?
    public Card createCard(CardRequest cardRequest) {
        Card card = Card.builder()
                // TODO: check if card number already exists?
                .number(encryptor.encrypt(cardRequest.cardNumber()))
                .maskedNumber(cardRequest.cardNumber().substring(12, 16))
                // TODO: transfer to UserService
                .userId(userRepository.findByPhoneNumber(cardRequest.ownerPhoneNumber()).orElseThrow(() ->
                        new ResourceNotFoundException(USER, PHONE_NUMBER, cardRequest.ownerPhoneNumber())
                ).getId())
                // TODO: validation
                .expirationDate(cardRequest.expirationDate())
                .build();

        return cardRepository.save(card);
    }

    public Page<CardResponse> getAllCardsWithFiler(Pageable pageable, CardFilter filter) {
        Specification<Card> spec = CardSpecifications.withFilter(filter);

        Page<Card> pagedCards = cardRepository.findAll(spec, pageable);
        return pagedCards.map(cardMapper::toCardResponse);
    }
}
