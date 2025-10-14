package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.card_operation.BlockCardOperation;
import com.example.bankcards.exception.AccessDeniedException;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.repository.CardOperationRepository;
import com.example.bankcards.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CardOperationService {
    @Autowired
    private CardOperationRepository cardOperationRepository;
    @Autowired
    private CardService cardService;

    public BlockCardOperation createBlockCardOperation(Long cardId, UserPrincipal userPrincipal) {
        Card card = cardService.findCardById(cardId);
        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new BadRequestException("Card is not ACTIVE");
        }
        if (!Objects.equals(card.getUserId(), userPrincipal.getId())) {
            throw new AccessDeniedException();
        }

        return cardOperationRepository.save(new BlockCardOperation(userPrincipal.getId(), cardId));
    }
}
