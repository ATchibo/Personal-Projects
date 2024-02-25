package com.example.assignment1_1.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class TokenRefreshResponse {
    @Getter
    @Setter
    private String accessToken;

    @Getter
    @Setter
    private String refreshToken;
}
