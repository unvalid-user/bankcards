package com.example.bankcards.service;

import com.example.bankcards.dto.request.CreateTransactionRequest;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.dto.filter.TransactionFilter;
import com.example.bankcards.repository.specification.TransactionSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.example.bankcards.util.AppConst.ID;
import static com.example.bankcards.util.AppConst.TRANSACTION;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardService cardService;

    public Page<Transaction> getTransactionsByUserId(Pageable pageable, TransactionFilter filter, Long userId) {
        filter.setUserId(userId);
        return findTransactionsWithSpecification(pageable, filter);
    }

    public Transaction getTransactionByIdAndUser(Long trId, Long userId) {
        return findTransactionByIdAndUser(trId, userId);
    }

    public Page<Transaction> getAllTransactions(Pageable pageable, TransactionFilter filter) {
        return findTransactionsWithSpecification(pageable, filter);
    }

    public Transaction createTransaction(CreateTransactionRequest request, Long userId) {
        Transaction transaction = Transaction.builder()
                .userId(userId)
                .sourceCardId(request.sourceCardId())
                .destinationCardId(request.destinationCardId())
                .monetaryAmount(request.monetaryAmount())
                .build();

        cardService.transferMoney(
                request.sourceCardId(),
                request.destinationCardId(),
                request.monetaryAmount(),
                userId
        );

        return transactionRepository.save(transaction);
    }


    private Page<Transaction> findTransactionsWithSpecification(Pageable pageable, TransactionFilter filter) {
        Specification<Transaction> spec = TransactionSpecifications.withFilter(filter);
        return transactionRepository.findAll(spec, pageable);
    }
    private Transaction findTransactionByIdAndUser(Long trId, Long userId) {
        return transactionRepository.findByIdAndUserId(trId, userId).orElseThrow(() ->
                new ResourceNotFoundException(TRANSACTION, ID, trId));
    }
}
