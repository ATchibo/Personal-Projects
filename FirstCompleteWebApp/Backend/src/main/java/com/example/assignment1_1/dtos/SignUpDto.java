package com.example.assignment1_1.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUpDto {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String role;
    private String location;
    private char[] password;
}
