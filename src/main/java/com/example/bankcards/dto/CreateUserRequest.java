package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import com.example.bankcards.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.example.bankcards.validation.ValidationMessage.PASSWORD_WRONG_LENGTH;

public record CreateUserRequest(
        @NotNull
        @ValidPhoneNumber
        String phoneNumber,

        @NotNull
        @Size(min=8, max=20, message = PASSWORD_WRONG_LENGTH)
        String password,

        @NotNull
        Role role
) {}
