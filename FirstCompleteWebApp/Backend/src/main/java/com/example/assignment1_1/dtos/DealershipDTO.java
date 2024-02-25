package com.example.assignment1_1.dtos;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
public class DealershipDTO {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String address;
    @Getter
    @Setter
    private String phone;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String website;
    @Getter
    @Setter
    private Long nrOfCars;
    @Getter
    @Setter
    private Long authorId;
    @Getter
    @Setter
    private String authorUsername;
}
