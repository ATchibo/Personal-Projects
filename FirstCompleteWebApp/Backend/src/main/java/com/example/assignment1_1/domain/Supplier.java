package com.example.assignment1_1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(indexes = {
        @Index(name = "idx_supplier_name", columnList = "name"),
        @Index(name = "idx_supplier_author", columnList = "fk_author_id")
    }
)
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "supplier_id")
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

    @OneToMany(targetEntity = ShippingContract.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_supplier_id", referencedColumnName = "supplier_id")
    @JsonIgnoreProperties("supplier")
    private List<ShippingContract> shippingContracts;

    @ManyToOne()
    @JoinColumn(name = "fk_author_id")
    @JsonIgnoreProperties({"cars", "employees", "shippingContracts", "dealerships", "suppliers"})
    @Getter
    @Setter
    private AppUser author;
}
