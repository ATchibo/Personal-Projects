package com.example.assignment1_1.exceptions;

public class CarNotFoundException extends RuntimeException{

    public CarNotFoundException(Long id) {
        super("Could not find car with id: " + id);
    }
}
