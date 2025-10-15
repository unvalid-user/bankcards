package com.example.bankcards.repository;

import com.example.bankcards.entity.card_operation.BlockCardOperation;
import com.example.bankcards.entity.card_operation.CardOperationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockCardOperationRepository extends JpaRepository<BlockCardOperation, Long> {
    boolean existsByStatusAndCardIdAndUserId(CardOperationStatus status, Long cardId, Long userId);
}
