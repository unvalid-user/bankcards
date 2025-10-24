package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.card_operation.BlockCardOperation;
import com.example.bankcards.entity.card_operation.CardOperationStatus;
import com.example.bankcards.exception.AccessDeniedException;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.exception.ResourceAlreadyExistsException;
import com.example.bankcards.repository.BlockCardOperationRepository;
import com.example.bankcards.repository.CardOperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CardOperationServiceTest {
    @Mock
    private CardOperationRepository cardOperationRepository;
    @Mock
    private BlockCardOperationRepository blockCardOperationRepository;
    @Mock
    private CardService cardService;

    @InjectMocks
    private CardOperationService cardOperationService;


    @Test
    void createBlockCardOperation_shouldCreateSuccessfully() {
        Long cardId = 4L;
        Long userId = 3L;
        Long bopId = 1L;

        Card card = Card.builder()
                .id(cardId)
                .userId(userId)
                .status(CardStatus.ACTIVE)
                .build();


        when(cardService.findCardById(cardId)).thenReturn(card);
        when(cardOperationRepository.save(any(BlockCardOperation.class))).thenAnswer((inv) -> {
            BlockCardOperation op = inv.getArgument(0);
            op.setId(bopId);
            return op;
        });
        when(blockCardOperationRepository.existsByStatusAndCardIdAndUserId(
                CardOperationStatus.PENDING,
                cardId,
                userId
        )).thenReturn(false);


        BlockCardOperation bop = cardOperationService.createBlockCardOperation(cardId, userId);


        assertEquals(bop.getCardId(), cardId);
        assertEquals(bop.getUserId(), userId);
        assertEquals(bop.getId(), bopId);
        assertEquals(bop.getStatus(), CardOperationStatus.PENDING);
    }

    @Test
    void createBlockCardOperation_shouldThrowWhenCardNotBelongToUser() {
        Long cardId = 4L;
        Long userId = 3L;

        Card card = Card.builder()
                .id(cardId)
                .userId(2L)
                .status(CardStatus.ACTIVE)
                .build();


        when(cardService.findCardById(cardId)).thenReturn(card);


        assertThrows(AccessDeniedException.class, () ->
                cardOperationService.createBlockCardOperation(cardId, userId)
        );
    }

    @Test
    void createBlockCardOperation_shouldThrowWhenCardNotActive() {
        Long cardId = 4L;
        Long userId = 3L;

        Card card = Card.builder()
                .id(cardId)
                .userId(userId)
                .status(CardStatus.EXPIRED)
                .build();


        when(cardService.findCardById(cardId)).thenReturn(card);


        assertThrows(BadRequestException.class, () ->
                cardOperationService.createBlockCardOperation(cardId, userId)
        );
    }

    @Test
    void createBlockCardOperation_shouldThrowWhenCardOperationAlreadyExists() {
        Long cardId = 4L;
        Long userId = 3L;

        Card card = Card.builder()
                .id(cardId)
                .userId(userId)
                .status(CardStatus.ACTIVE)
                .build();


        when(cardService.findCardById(cardId)).thenReturn(card);
        when(blockCardOperationRepository.existsByStatusAndCardIdAndUserId(
                CardOperationStatus.PENDING,
                cardId,
                userId
        )).thenReturn(true);


        assertThrows(ResourceAlreadyExistsException.class, () ->
                cardOperationService.createBlockCardOperation(cardId, userId)
        );
    }
}
