package com.example.assignment1_1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class DealershipNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(DelershipNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(DelershipNotFoundException ex) {
        return ex.getMessage();
    }
}
