package com.example.assignment1_1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(indexes = {
        @Index(name = "idx_car_dealership", columnList = "fk_dealership_id"),
        @Index(name = "idx_car_price", columnList = "price"),
        @Index(name = "idx_car_author", columnList = "fk_author_id")
})
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "car_id")
    @Getter
    @Setter
    private Long id;

    @Column(length = 50)
    @Getter
    @Setter
    private String brand;

    @Column(length = 100)
    @Getter
    @Setter
    private String model;

    @Column
    @Getter
    @Setter
    private Integer year;

    @Column(length = 50)
    @Getter
    @Setter
    private String color;

    @Column
    @Getter
    @Setter
    private Integer price;

    @Column(length = 1000)
    @Getter
    @Setter
    private String description;

    @ManyToOne()
    @JoinColumn(name = "fk_dealership_id")
    @JsonIgnoreProperties({"cars", "employees", "shippingContracts"})
    @Getter
    @Setter
    private Dealership dealership;

    @ManyToOne()
    @JoinColumn(name = "fk_author_id")
    @JsonIgnoreProperties({"cars", "employees", "shippingContracts", "dealerships", "suppliers"})
    @Getter
    @Setter
    private AppUser author;

    public Car(String brand, String model, Integer year, String color, Integer price, String description, Dealership dealership, AppUser author) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.price = price;
        this.description = description;
        this.dealership = dealership;
        this.author = author;
    }
}
