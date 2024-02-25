package com.example.assignment1_1.exceptions;

import org.springframework.http.HttpStatus;


public class AppException extends RuntimeException {

    private final HttpStatus code;

    public AppException(String message, HttpStatus httpStatus) {
        super(message);
        this.code = httpStatus;
    }

    public HttpStatus getCode() {
        return code;
    }
}
