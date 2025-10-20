package com.example.bankcards.repository.specification;

import com.example.bankcards.dto.filter.TransactionFilter;
import com.example.bankcards.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecifications {
    public static Specification<Transaction> withFilter(TransactionFilter filter) {
        return Specification.allOf(
                withUserId(filter.getUserId()),
                withSourceCardId(filter.getSourceCardId()),
                withDestinationCardId(filter.getDestinationCardId())
        );
    }

    private static Specification<Transaction> withUserId(Long userId) {
        return (root, query, cb) ->
                userId == null ? null : cb.equal(root.get("userId"), userId);
    }
    private static Specification<Transaction> withSourceCardId(Long sourceCardId) {
        return (root, query, cb) ->
                sourceCardId == null ? null : cb.equal(root.get("sourceCardId"), sourceCardId);
    }
    private static Specification<Transaction> withDestinationCardId(Long destinationCardId) {
        return (root, query, cb) ->
                destinationCardId == null ? null : cb.equal(root.get("destinationCardId"), destinationCardId);
    }
}
