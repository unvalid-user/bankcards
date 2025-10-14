package com.example.bankcards.repository.specification;

import com.example.bankcards.entity.Role;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserFilter {
    private Long id;
    private String phoneNumber;
    private Role role;
}
