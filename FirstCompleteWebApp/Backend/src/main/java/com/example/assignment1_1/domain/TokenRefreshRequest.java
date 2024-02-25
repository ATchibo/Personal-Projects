package com.example.assignment1_1.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class TokenRefreshRequest {

    @NotBlank
    @Getter
    @Setter
    private String refreshToken;
}
