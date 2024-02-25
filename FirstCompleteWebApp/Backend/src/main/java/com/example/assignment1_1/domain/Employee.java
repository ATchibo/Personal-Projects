package com.example.assignment1_1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(indexes = {
        @Index(name = "idx_employee_dealership", columnList = "fk_dealership_id"),
        @Index(name = "idx_employee_author", columnList = "fk_author_id")
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "employee_id")
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotEmpty(message = "Please provide a valid employee name")
    private String name;

    @Getter
    @Setter
    @NotEmpty(message = "Please provide a valid employee role")
    private String role;

    @Getter
    @Setter
    @Email(message = "Please provide a valid employee email")
    private String email;

    @Getter
    @Setter
    @NotEmpty(message = "Please provide a valid employee phone number")
    private String phone;

    @Getter
    @Setter
    @Min(value = 100, message = "Please provide a valid employee salary (at least 100)")
    private Integer salary;

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
}
