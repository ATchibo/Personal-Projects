package com.example.assignment1_1.exceptions;

public class DelershipNotFoundException extends RuntimeException {
    public DelershipNotFoundException(Long id) {
        super("Could not find dealership " + id);
    }
}
