package com.example.assignment1_1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    public TokenRefreshException(String token, String message) {
        super("Failed for [" + token + "]: " + message);
    }
}
