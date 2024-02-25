package com.example.assignment1_1.dtos;

import com.example.assignment1_1.domain.Dealership;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class EmployeeDTO {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String role;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private Integer salary;

    @Getter
    @Setter
    private Long dealershipId;

    @Getter
    @Setter
    private String dealershipName;

    @Getter
    @Setter
    private Long authorId;

    @Getter
    @Setter
    private String authorUsername;
}
