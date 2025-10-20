package com.example.bankcards.service;

import com.example.bankcards.dto.request.CreateTransactionRequest;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CardService cardService;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    void createTransaction_shouldCreateSuccessfully() {
        Long userId = 3L;
        Long sourceCardId = 4L;
        Long destinationCardId = 6L;
        Long transactionId = 1L;
        BigDecimal monetaryAmount = BigDecimal.valueOf(100.55);

        CreateTransactionRequest request = new CreateTransactionRequest(
                sourceCardId,
                destinationCardId,
                monetaryAmount
        );


        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction tr = inv.getArgument(0);
            tr.setId(transactionId);
            return tr;
        });


        Transaction transaction = transactionService.createTransaction(request, userId);


        assertEquals(transaction.getId(), transactionId);
        assertEquals(transaction.getUserId(), userId);
        assertEquals(transaction.getSourceCardId(), sourceCardId);
        assertEquals(transaction.getDestinationCardId(), destinationCardId);
    }
}
