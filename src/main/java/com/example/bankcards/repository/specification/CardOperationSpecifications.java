package com.example.bankcards.repository.specification;

import com.example.bankcards.dto.filter.CardOperationFilter;
import com.example.bankcards.entity.card_operation.CardOperation;
import com.example.bankcards.entity.card_operation.CardOperationStatus;
import org.springframework.data.jpa.domain.Specification;

public class CardOperationSpecifications {
    public static Specification<CardOperation> withFilter(CardOperationFilter filter) {
        return Specification.allOf(
                withUserId(filter.getUserId()),
                withStatus(filter.getStatus())
        );
    }

    private static Specification<CardOperation> withUserId(Long userId) {
        return (root, query, cb) ->
                userId == null ? null : cb.equal(root.get("userId"), userId);
    }
    private static Specification<CardOperation> withStatus(CardOperationStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status.name());
    }
}
