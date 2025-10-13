package com.example.bankcards.entity.card_request;

import com.example.bankcards.entity.Card;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "block_card_requests")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockCardRequest extends CardRequest{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", insertable = false, updatable = false)
    private Card card;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    public BlockCardRequest(Long userId, Long cardId) {
        super(userId);
        this.cardId = cardId;
    }
}
