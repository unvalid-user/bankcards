package com.example.bankcards.dto.filter;

import com.example.bankcards.entity.RoleName;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserFilter {
    private Long id;
    private String phoneNumber;
    private RoleName role;
}
