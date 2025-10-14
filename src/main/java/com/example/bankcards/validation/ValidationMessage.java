package com.example.bankcards.validation;

public final class ValidationMessage {
    public static final int PASSWORD_MIN = 8;
    public static final int PASSWORD_MAX = 20;

    public static final String CARD_NUMBER_INVALID_FORMAT = "Wrong card number format";
    public static final String PHONE_NUMBER_INVALID_FORMAT = "Wrong phone number format";
    public static final String EXPIRATION_DATE_MUST_BE_FUTURE = "Expiration Date must be in the future";
    public static final String PASSWORD_WRONG_LENGTH = "Password should be at least " + PASSWORD_MIN
            + " characters and not longer than " + PASSWORD_MAX + " characters";
}
