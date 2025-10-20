package com.example.bankcards.dto.response;


public record UserResponse (
        Long id,
        String phoneNumber,
        String role
) {}
