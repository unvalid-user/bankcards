package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.security.UserPrincipal;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    private CardService cardService;

    @Autowired
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
}
