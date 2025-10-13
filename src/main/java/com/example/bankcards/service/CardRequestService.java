package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.card_request.BlockCardRequest;
import com.example.bankcards.exception.AccessDeniedException;
import com.example.bankcards.repository.CardRequestRepository;
import com.example.bankcards.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CardRequestService {
    @Autowired
    private CardRequestRepository cardRequestRepository;
    @Autowired
    private CardService cardService;

    public BlockCardRequest createBlockCardRequest(Long cardId, UserPrincipal userPrincipal) {
        Card card = cardService.findCardById(cardId);
        if (!Objects.equals(card.getUserId(), userPrincipal.getId())) {
            throw new AccessDeniedException();
        }

        return cardRequestRepository.save(new BlockCardRequest(userPrincipal.getId(), cardId));
    }
}
