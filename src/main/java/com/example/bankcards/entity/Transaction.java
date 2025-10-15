package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id", nullable = false)
    private Long userId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_card_id", insertable = false, updatable = false)
    private Card sourceCard;

    @Column(name = "source_card_id", nullable = false)
    private Long sourceCardId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_card_id", insertable = false, updatable = false)
    private Card destinationCard;

    @Column(name = "destination_card_id", nullable = false)
    private Long destinationCardId;


    @Column(nullable = false)
    private BigDecimal monetaryAmount;
}
