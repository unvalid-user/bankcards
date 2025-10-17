package com.example.bankcards.dto.mapper;

import com.example.bankcards.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    default String map(Role role) {
        return role == null ? null : role.getName().name();
    }
}
