package com.example.bankcards.repository;

import com.example.bankcards.entity.card_operation.CardOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CardOperationRepository extends JpaRepository<CardOperation, Long>, JpaSpecificationExecutor<CardOperation> {
}
