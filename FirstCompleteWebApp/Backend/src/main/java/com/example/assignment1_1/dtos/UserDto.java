package com.example.assignment1_1.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    public UserDto(Long id, String firstName, String lastName, String email, String username, String role, String location) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.role = role;
        this.location = location;
        this.token = "";
        this.refreshToken = "";
    }

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String role;
    private String location;
    private String token;
    private String refreshToken;
}
