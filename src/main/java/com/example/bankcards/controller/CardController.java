package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.UpdateCardRequest;
import com.example.bankcards.entity.card_operation.BlockCardOperation;
import com.example.bankcards.entity.card_operation.CardOperation;
import com.example.bankcards.repository.specification.CardFilter;
import com.example.bankcards.security.UserPrincipal;
import com.example.bankcards.service.CardOperationService;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.example.bankcards.util.AppConst.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private CardOperationService cardOperationService;

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(
            @PathVariable("id") Long cardId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        CardResponse card = cardService.getCardById(cardId, userPrincipal.getId());

        return ResponseEntity.ok(card);
    }

    @GetMapping
    @Secured("ROLE_USER")
    public ResponseEntity<Page<CardResponse>> getCardsByUser(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
            @ModelAttribute CardFilter cardFilter,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Page<CardResponse> pagedCards = cardService.getCardsByUserId(pageable, cardFilter, userPrincipal.getId());

        return ResponseEntity.ok(pagedCards);
    }

    // TODO: CardOperationController?
    @PostMapping("/{id}/request-block")
    @Secured("ROLE_USER")
    public ResponseEntity<CardOperation> operationBlockCard(
            @PathVariable("id") Long cardId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        BlockCardOperation createdCardOperation = cardOperationService.createBlockCardOperation(cardId, userPrincipal);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/card-operations/{id}")
                .buildAndExpand(createdCardOperation.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(createdCardOperation);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CardResponse> createCard(
            @Valid @RequestBody CreateCardRequest createCardRequest
    ) {
        CardResponse createdCard = cardService.createCard(createCardRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/cards/{id}")
                .buildAndExpand(createdCard.id())
                .toUri();

        return ResponseEntity.created(location)
                .body(createdCard);
    }

    // TODO:
    //  @RequestParam Sort
    //  Validation
    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Page<CardResponse>> getAllCards(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
            @ModelAttribute CardFilter cardFilter
    ) {
        Page<CardResponse> pagedCards = cardService.getCardsWithFilter(pageable, cardFilter);

        return ResponseEntity.ok(pagedCards);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteCard(
            @PathVariable("id") Long cardId
    ) {
        cardService.deleteCardById(cardId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CardResponse> updateCard(
            @PathVariable("id") Long cardId,
            @RequestBody UpdateCardRequest updateCardRequest
    ) {
        CardResponse card = cardService.updateCard(cardId, updateCardRequest);

        return ResponseEntity.ok(card);
    }
}
