package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.repository.specification.CardFilter;
import com.example.bankcards.security.UserPrincipal;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.AppConst;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.example.bankcards.util.AppConst.DEFAULT_PAGE_NUMBER;
import static com.example.bankcards.util.AppConst.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    // TODO: transfer to Service layer
    private CardMapper cardMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(
            @PathVariable("id") Long cardId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Card card = cardService.getCardById(cardId, userPrincipal.getId());

        return ResponseEntity.ok(cardMapper.toCardResponse(card));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardResponse> createCard(
            @Valid @RequestBody CardRequest cardRequest
    ) {
        Card createdCard = cardService.createCard(cardRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/cards/{id}")
                .buildAndExpand(createdCard.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(cardMapper.toCardResponse(createdCard));
    }

    // TODO:
    //  @RequestParam Sort
    //  Validation
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CardResponse>> getAllCards(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable,
//            @RequestParam(name = "page", defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
//            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size,
            @ModelAttribute CardFilter cardFilter
//            @RequestParam(name = "status", required = false) CardStatus status,
//            @RequestParam(name = "maskedNumber", required = false) String maskedNumber,
//            @RequestParam(name = "userId", required = false) Long userId
    ) {
        Page<CardResponse> pagedCards = cardService.getAllCardsWithFiler(pageable, cardFilter);

        return ResponseEntity.ok(pagedCards);
    }
}
