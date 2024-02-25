package com.example.assignment1_1.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CarDTO {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String brand;

    @Getter
    @Setter
    private String model;

    @Getter
    @Setter
    private Integer year;

    @Getter
    @Setter
    private String color;

    @Getter
    @Setter
    private Integer price;

    @Getter
    @Setter
    private String description;

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
