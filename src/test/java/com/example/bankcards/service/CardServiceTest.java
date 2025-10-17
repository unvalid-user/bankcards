package com.example.bankcards.service;

import com.example.bankcards.dto.card.CreateCardRequest;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.Encryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserService userService;
    @Mock
    private Encryptor encryptor;
    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;


    @Test
    void createCard_shouldCreateCardSuccessfully() {
        String cardNumber = "1234123412340987";
        String maskedNumber = "0987";
        String encrypted = "encrypted";
        Long createdCardId = 1L;
        Long userId = 3L;
        String userPhoneNumber = "89123456789";
        LocalDate expDate = LocalDate.of(2027, 10,13);

        CreateCardRequest request = new CreateCardRequest(
                cardNumber,
                userPhoneNumber,
                expDate
        );
        User user = User.builder()
                .id(userId)
                .phoneNumber(userPhoneNumber)
                .role(new Role(RoleName.ROLE_USER))
                .build();


        when(encryptor.encrypt(cardNumber)).thenReturn(encrypted);
        when(userService.findUserByPhoneNumber(userPhoneNumber)).thenReturn(user);
        when(cardRepository.save(any(Card.class))).thenAnswer(inv -> {
            Card card = inv.getArgument(0);
            card.setId(createdCardId);
            card.setBalance(BigDecimal.ZERO);
            return card;
        });


        Card card = cardService.createCard(request);


        assertEquals(card.getId(), createdCardId);
        assertEquals(card.getUserId(), userId);
        assertEquals(card.getNumber(), encrypted);
        assertEquals(card.getMaskedNumber(), maskedNumber);
        assertEquals(card.getExpirationDate(), expDate);
        assertEquals(card.getBalance(), BigDecimal.ZERO);
    }

    @Test
    void transferMoney_shouldTransferSuccessfully() {
        Long sourceCardId = 4L;
        Long destinationCardId = 6L;
        BigDecimal monetaryAmount = BigDecimal.valueOf(100.55);
        BigDecimal sourceCardBalance = BigDecimal.valueOf(400);
        BigDecimal destinationCardBalance = BigDecimal.ZERO;
        Long userId = 3L;

        Card sourceCard = Card.builder()
                .id(sourceCardId)
                .userId(userId)
                .balance(sourceCardBalance)
                .status(CardStatus.ACTIVE)
                .build();
        Card destinationCard = Card.builder()
                .id(sourceCardId)
                .userId(userId)
                .balance(destinationCardBalance)
                .status(CardStatus.ACTIVE)
                .build();


        when(cardRepository.save(any(Card.class))).thenAnswer(inv -> inv.<Card>getArgument(0));
        when(cardRepository.findByIdAndUserId(sourceCardId, userId)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findByIdAndUserId(destinationCardId, userId)).thenReturn(Optional.of(destinationCard));


        cardService.transferMoney(sourceCardId, destinationCardId, monetaryAmount, userId);


        assertEquals(sourceCard.getBalance(), sourceCardBalance.subtract(monetaryAmount));
        assertEquals(destinationCard.getBalance(), destinationCardBalance.add(monetaryAmount));
    }

    @Test
    void transferMoney_shouldThrowWhenInsufficientBalance() {
        Long sourceCardId = 4L;
        Long destinationCardId = 6L;
        BigDecimal monetaryAmount = BigDecimal.valueOf(100.55);
        BigDecimal sourceCardBalance = BigDecimal.valueOf(50);
        BigDecimal destinationCardBalance = BigDecimal.ZERO;
        Long userId = 3L;

        Card sourceCard = Card.builder()
                .id(sourceCardId)
                .userId(userId)
                .balance(sourceCardBalance)
                .status(CardStatus.ACTIVE)
                .build();
        Card destinationCard = Card.builder()
                .id(sourceCardId)
                .userId(userId)
                .balance(destinationCardBalance)
                .status(CardStatus.ACTIVE)
                .build();


        when(cardRepository.findByIdAndUserId(sourceCardId, userId)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findByIdAndUserId(destinationCardId, userId)).thenReturn(Optional.of(destinationCard));


        assertThrows(BadRequestException.class, () ->
                cardService.transferMoney(sourceCardId, destinationCardId, monetaryAmount, userId)
        );
    }

    @Test
    void transferMoney_shouldThrowWhenIdsEquals() {
        Long sourceCardId = 4L;
        Long destinationCardId = 4L;
        BigDecimal monetaryAmount = BigDecimal.valueOf(100.55);
        Long userId = 3L;

        assertThrows(BadRequestException.class, () ->
                cardService.transferMoney(sourceCardId, destinationCardId, monetaryAmount, userId)
        );
    }

    @Test
    void transferMoney_shouldThrowWhenCardIsNotActive() {
        Long sourceCardId = 4L;
        Long destinationCardId = 6L;
        BigDecimal monetaryAmount = BigDecimal.valueOf(100.55);
        BigDecimal sourceCardBalance = BigDecimal.valueOf(50);
        BigDecimal destinationCardBalance = BigDecimal.ZERO;
        Long userId = 3L;

        Card sourceCard = Card.builder()
                .id(sourceCardId)
                .userId(userId)
                .balance(sourceCardBalance)
                .status(CardStatus.ACTIVE)
                .build();
        Card destinationCard = Card.builder()
                .id(sourceCardId)
                .userId(userId)
                .balance(destinationCardBalance)
                .status(CardStatus.BLOCKED)
                .build();


        when(cardRepository.findByIdAndUserId(sourceCardId, userId)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findByIdAndUserId(destinationCardId, userId)).thenReturn(Optional.of(destinationCard));


        assertThrows(BadRequestException.class, () ->
                cardService.transferMoney(sourceCardId, destinationCardId, monetaryAmount, userId)
        );
    }
}
