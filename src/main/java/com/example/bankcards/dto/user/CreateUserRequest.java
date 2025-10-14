package com.example.bankcards.dto.user;

import com.example.bankcards.entity.Role;
import com.example.bankcards.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.example.bankcards.validation.ValidationMessage.*;

public record CreateUserRequest(
        @NotNull
        @ValidPhoneNumber
        String phoneNumber,

        @NotNull
        @Size(min=PASSWORD_MIN, max=PASSWORD_MAX, message = PASSWORD_WRONG_LENGTH)
        String password,

        @NotNull
        Role role
) {}
