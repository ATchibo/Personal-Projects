package com.example.assignment1_1.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class SupplierDTO {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String phone;
    @Getter
    @Setter
    private Long nrContracts;
    @Getter
    @Setter
    private Long authorId;
    @Getter
    @Setter
    private String authorUsername;
}
