package com.mar.solutions.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationResponse {
    private final String accessToken;
    private final String tokenType = "Bearer";
    //private final long expiresIn;
}
