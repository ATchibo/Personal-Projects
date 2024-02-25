package com.example.assignment1_1.exceptions;

public class SupplierNotFoundException extends RuntimeException {
    public SupplierNotFoundException(Long id) {
        super("Could not find supplier " + id);
    }
}
