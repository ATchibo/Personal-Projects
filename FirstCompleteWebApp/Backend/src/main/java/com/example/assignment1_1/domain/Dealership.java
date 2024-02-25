package com.example.assignment1_1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(indexes = {
        @Index(name = "idx_dealership_id_name", columnList = "dealership_id, name"),
        @Index(name = "idx_dealership_name", columnList = "name"),
        @Index(name = "idx_dealership_author", columnList = "fk_author_id")
})
public class Dealership {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "dealership_id")
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotEmpty(message = "Name is mandatory")
    private String name;
    @Getter
    @Setter
    @NotEmpty(message = "Address is mandatory")
    private String address;
    @Getter
    @Setter
    @NotEmpty(message = "Phone is mandatory")
    private String phone;
    @Getter
    @Setter
    @NotEmpty(message = "Please provide a valid email")
    private String email;
    @Getter
    @Setter
    private String website;

    @OneToMany(targetEntity = Car.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_dealership_id", referencedColumnName = "dealership_id")
    @Getter
    @Setter
    private List<Car> cars;

    @OneToMany(targetEntity = Employee.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_dealership_id", referencedColumnName = "dealership_id")
    @Getter
    @Setter
    private List<Employee> employees;

    @OneToMany(targetEntity = ShippingContract.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_dealership_id", referencedColumnName = "dealership_id")
    @JsonIgnoreProperties("dealership")
    @Getter
    @Setter
    private List<ShippingContract> shippingContracts;

    @ManyToOne()
    @JoinColumn(name = "fk_author_id")
    @JsonIgnoreProperties({"cars", "employees", "shippingContracts", "dealerships", "suppliers"})
    @Getter
    @Setter
    private AppUser author;
}
