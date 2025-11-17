package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.card_operation.BlockCardOperation;
import com.example.bankcards.entity.card_operation.CardOperation;
import com.example.bankcards.entity.card_operation.CardOperationStatus;
import com.example.bankcards.exception.*;
import com.example.bankcards.repository.BlockCardOperationRepository;
import com.example.bankcards.repository.CardOperationRepository;
import com.example.bankcards.dto.filter.CardOperationFilter;
import com.example.bankcards.repository.specification.CardOperationSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.example.bankcards.util.AppConst.*;

@Service
@RequiredArgsConstructor
public class CardOperationService {
    private final CardOperationRepository cardOperationRepository;
    private final BlockCardOperationRepository blockCardOperationRepository;
    private final CardService cardService;

    public BlockCardOperation createBlockCardOperation(Long cardId, Long userId) {
        Card card = cardService.findCardById(cardId);
        if (!Objects.equals(card.getUserId(), userId)) {
            throw new AccessDeniedException();
        }
        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new BadRequestException("Card is not ACTIVE");
        }
        blockOperationShouldNotExist(cardId, userId);

        return cardOperationRepository.save(new BlockCardOperation(userId, cardId));
    }

    public CardOperation cancelCardOperation(Long cardOperationId) {
        return changeCardOperationStatus(cardOperationId, CardOperationStatus.CANCELED);
    }

    public CardOperation completeCardOperation(Long cardOperationId) {
        return changeCardOperationStatus(cardOperationId, CardOperationStatus.COMPLETED);
    }

    private CardOperation changeCardOperationStatus(Long cardOperationId, CardOperationStatus status) {
        CardOperation cardOperation = findCardOperationById(cardOperationId);

        if (cardOperation.getStatus() != CardOperationStatus.PENDING)
            throw new ConflictException("Card operation current status is not PENDING");

        cardOperation.setStatus(status);
        return cardOperationRepository.save(cardOperation);
    }

    private CardOperation findCardOperationById(Long cardOperationId) {
        return cardOperationRepository.findById(cardOperationId).orElseThrow(() ->
                new ResourceNotFoundException(CARD_OPERATION, ID, cardOperationId));
    }

    public Page<CardOperation> getCardOperationsWithFilter(Pageable pageable, CardOperationFilter filter) {
        return findCardOperationsWithSpecification(pageable, filter);
    }

    private Page<CardOperation> findCardOperationsWithSpecification(Pageable pageable, CardOperationFilter filter) {
        Specification<CardOperation> spec = CardOperationSpecifications.withFilter(filter);
        return cardOperationRepository.findAll(spec, pageable);
    }

    private void blockOperationShouldNotExist(Long cardId, Long userId) {
        if (blockCardOperationRepository.existsByStatusAndCardIdAndUserId(
                CardOperationStatus.PENDING,
                cardId,
                userId
        )) {
            throw new ResourceAlreadyExistsException(BLOCK_CARD_OPERATION, CARD_ID, cardId);
        }
    }

    public CardOperation getCardOperationById(Long cardOperationId) {
        return findCardOperationById(cardOperationId);
    }
}
