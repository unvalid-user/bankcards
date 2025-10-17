package com.example.bankcards.repository.specification;

import com.example.bankcards.entity.RoleName;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> withFilter(UserFilter filter) {
        return Specification.allOf(
                withPhoneNumber(filter.getPhoneNumber()),
                withRole(filter.getRole())
        );
    }

    private static Specification<User> withPhoneNumber(String phoneNumber) {
        return (root, query, cb) ->
                phoneNumber == null ? null : cb.equal(root.get("phoneNumber"), phoneNumber);
    }
    private static Specification<User> withRole(RoleName role) {
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.get("role"), role.name());
    }
}
