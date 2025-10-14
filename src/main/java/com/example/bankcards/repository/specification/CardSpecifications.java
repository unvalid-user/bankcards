package com.example.bankcards.repository.specification;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.jpa.domain.Specification;

public class CardSpecifications {
    public static Specification<Card> withFilter(CardFilter filter) {
        return Specification.allOf(
                withMaskedNumber(filter.getMaskedNumber()),
                withUserId(filter.getUserId()),
                withStatus(filter.getStatus())
        );
    }

    private static Specification<Card> withMaskedNumber(String maskedNumber) {
        return (root, query, cb) ->
                maskedNumber == null ? null : cb.equal(root.get("maskedNumber"), maskedNumber);
    }
    private static Specification<Card> withUserId(Long userId) {
        return (root, query, cb) ->
                userId == null ? null : cb.equal(root.get("userId"), userId);
    }
    private static Specification<Card> withStatus(CardStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status.name());
    }
}
