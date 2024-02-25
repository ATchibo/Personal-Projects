package com.example.assignment1_1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SupplierNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(SupplierNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String supplierNotFoundHandler(SupplierNotFoundException ex) {
        return ex.getMessage();
    }
}
