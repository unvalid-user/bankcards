package com.example.bankcards.dto.user;


public record UserResponse (
        Long id,
        String phoneNumber,
        String role
) {}
