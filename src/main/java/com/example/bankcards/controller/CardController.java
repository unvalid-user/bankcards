package com.example.bankcards.controller;

import com.example.bankcards.dto.PagedResponse;
import com.example.bankcards.dto.card.CreateCardRequest;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.UpdateCardRequest;
import com.example.bankcards.dto.card_operation.CardOperationResponse;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.dto.mapper.CardOperationMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.card_operation.BlockCardOperation;
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

// TODO:
//  - restrict sort params
//  - add card transactions

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private CardOperationService cardOperationService;
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private CardOperationMapper cardOperationMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(
            @PathVariable("id") Long cardId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Card card = cardService.getCardById(cardId, userPrincipal.getId());

        return ResponseEntity.ok(cardMapper.toResponse(card));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<CardResponse>> getCardsByUser(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
            @ModelAttribute CardFilter cardFilter,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Page<Card> pageCards = cardService.getCardsByUserId(pageable, cardFilter, userPrincipal.getId());

        return ResponseEntity.ok(cardMapper.toPagedResponse(pageCards));
    }

    @PostMapping("/{id}/request-block")
    public ResponseEntity<CardOperationResponse> requestBlockCard(
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
                .body(cardOperationMapper.toResponse(createdCardOperation));
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CardResponse> createCard(
            @Valid @RequestBody CreateCardRequest createCardRequest
    ) {
        Card createdCard = cardService.createCard(createCardRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/cards/{id}")
                .buildAndExpand(createdCard.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(cardMapper.toResponse(createdCard));
    }

    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<PagedResponse<CardResponse>> getAllCards(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
            @ModelAttribute CardFilter cardFilter
    ) {
        Page<Card> pageCards = cardService.getCardsWithFilter(pageable, cardFilter);

        return ResponseEntity.ok(cardMapper.toPagedResponse(pageCards));
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
            @Valid @RequestBody UpdateCardRequest updateCardRequest
    ) {
        Card card = cardService.updateCard(cardId, updateCardRequest);

        return ResponseEntity.ok(cardMapper.toResponse(card));
    }
}
