package com.example.assignment1_1.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
public class ErrorMessage {

    @Getter
    private int statusCode;
    @Getter
    private Date timestamp;
    @Getter
    private String message;
    @Getter
    private String description;
}
