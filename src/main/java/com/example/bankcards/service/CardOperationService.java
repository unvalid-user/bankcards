package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.card_operation.BlockCardOperation;
import com.example.bankcards.entity.card_operation.CardOperation;
import com.example.bankcards.entity.card_operation.CardOperationStatus;
import com.example.bankcards.exception.*;
import com.example.bankcards.repository.BlockCardOperationRepository;
import com.example.bankcards.repository.CardOperationRepository;
import com.example.bankcards.repository.specification.CardOperationFilter;
import com.example.bankcards.repository.specification.CardOperationSpecifications;
import com.example.bankcards.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.example.bankcards.util.AppConst.*;

@Service
public class CardOperationService {
    @Autowired
    private CardOperationRepository cardOperationRepository;
    @Autowired
    private BlockCardOperationRepository blockCardOperationRepository;
    @Autowired
    private CardService cardService;

    public BlockCardOperation createBlockCardOperation(Long cardId, UserPrincipal userPrincipal) {
        Card card = cardService.findCardById(cardId);
        if (!Objects.equals(card.getUserId(), userPrincipal.getId())) {
            throw new AccessDeniedException();
        }
        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new BadRequestException("Card is not ACTIVE");
        }
        blockOperationShouldNotExist(cardId, userPrincipal.getId());

        return cardOperationRepository.save(new BlockCardOperation(userPrincipal.getId(), cardId));
    }

    public CardOperation cancelCardOperation(Long cardOperationId) {
        return changeCardOperationStatus(cardOperationId, CardOperationStatus.COMPLETED);
    }

    public CardOperation completeCardOperation(Long cardOperationId) {
        return changeCardOperationStatus(cardOperationId, CardOperationStatus.CANCELED);
    }

    private CardOperation changeCardOperationStatus(Long cardOperationId, CardOperationStatus status) {
        CardOperation cardOperation = findCardOperationById(cardOperationId);

        if (cardOperation.getStatus() != CardOperationStatus.PENDING)
            throw new ConflictException("Card operation current status in not PENDING");

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
            throw new ResourceAlreadyExists(BLOCK_CARD_OPERATION, CARD_ID, cardId);
        }
    }
}
