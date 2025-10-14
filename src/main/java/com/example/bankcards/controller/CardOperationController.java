package com.example.bankcards.controller;

import com.example.bankcards.entity.card_operation.CardOperation;
import com.example.bankcards.repository.specification.CardOperationFilter;
import com.example.bankcards.service.CardOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.example.bankcards.util.AppConst.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/card-operations")
@Secured("ROLE_ADMIN")
public class CardOperationController {
    @Autowired
    private CardOperationService cardOperationService;


    @PatchMapping("/{id}/cancel")
    public ResponseEntity<CardOperation> cancelCardOperation(
            @PathVariable("id") Long cardOperationId
    ) {
        CardOperation cardOperation = cardOperationService.cancelCardOperation(cardOperationId);

        return ResponseEntity.ok(cardOperation);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<CardOperation> completeCardOperation(
            @PathVariable("id") Long cardOperationId
    ) {
        CardOperation cardOperation = cardOperationService.completeCardOperation(cardOperationId);

        return ResponseEntity.ok(cardOperation);
    }

    // TODO: "type" field in JSON
    @GetMapping
    public ResponseEntity<Page<CardOperation>> getAllCardOperations(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
            @ModelAttribute CardOperationFilter cardOperationFilter
    ) {
        Page<CardOperation> pagedCardOperations = cardOperationService.getCardOperationsWithFilter(pageable, cardOperationFilter);

        return ResponseEntity.ok(pagedCardOperations);
    }
}
