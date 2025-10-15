package com.example.bankcards.controller;

import com.example.bankcards.dto.PagedResponse;
import com.example.bankcards.dto.transaction.CreateTransactionRequest;
import com.example.bankcards.dto.transaction.TransactionResponse;
import com.example.bankcards.dto.mapper.TransactionMapper;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.repository.specification.TransactionFilter;
import com.example.bankcards.security.UserPrincipal;
import com.example.bankcards.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.bankcards.util.AppConst.DEFAULT_PAGE_SIZE;

@RequestMapping("/transactions")
@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionMapper transactionMapper;


    @GetMapping()
    public ResponseEntity<PagedResponse<TransactionResponse>> getTransactionsByUser(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
            @ModelAttribute TransactionFilter transactionFilter,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Page<Transaction> pageTransactions = transactionService.getTransactionsByUserId(pageable, transactionFilter,userPrincipal.getId());

        return ResponseEntity.ok(transactionMapper.toPagedResponse(pageTransactions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable("id") Long transactionId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Transaction transaction = transactionService.getTransactionById(transactionId, userPrincipal.getId());

        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest createTransactionRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Transaction transaction = transactionService.createTransaction(createTransactionRequest, userPrincipal.getId());

        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<PagedResponse<TransactionResponse>> getAllTransactions(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
            @ModelAttribute TransactionFilter transactionFilter
    ) {
        Page<Transaction> pageTransactions = transactionService.getAllTransactions(pageable, transactionFilter);

        return ResponseEntity.ok(transactionMapper.toPagedResponse(pageTransactions));
    }
}
