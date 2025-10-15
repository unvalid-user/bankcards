package com.example.bankcards.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthResponse {
    private final String accessToken;
    private final String tokenType = "Bearer";

    public JwtAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
