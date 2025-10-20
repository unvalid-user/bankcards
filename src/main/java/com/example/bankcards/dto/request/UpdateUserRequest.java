package com.example.bankcards.dto.request;

import com.example.bankcards.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.Size;

import static com.example.bankcards.validation.ValidationMessage.*;

public record UpdateUserRequest (
        @ValidPhoneNumber
        String phoneNumber,

        @Size(min=PASSWORD_MIN, max=PASSWORD_MAX, message = PASSWORD_WRONG_LENGTH)
        String password
) {}
