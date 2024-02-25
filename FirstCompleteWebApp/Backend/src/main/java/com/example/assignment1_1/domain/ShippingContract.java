package com.example.assignment1_1.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(indexes = {
    @Index(name = "idx_contract_supplier", columnList = "fk_supplier_id"),
    @Index(name = "idx_contract_dealership", columnList = "fk_dealership_id"),
    @Index(name = "idx_contract_author", columnList = "fk_author_id")
})
public class ShippingContract {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "contract_id")
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Date contractDate;

    @Getter
    @Setter
    private Integer contractYearsDuration;

    public ShippingContract(Date contractDate, Integer contractYearsDuration) {
        this.contractDate = contractDate;
        this.contractYearsDuration = contractYearsDuration;
    }

    public ShippingContract(Long id, Date contractDate, Integer contractYearsDuration) {
        this.id = id;
        this.contractDate = contractDate;
        this.contractYearsDuration = contractYearsDuration;
    }

    @ManyToOne
    @JoinColumn(name = "fk_dealership_id")
    @JsonIgnoreProperties({"cars", "employees", "shippingContracts"})
    @Getter
    @Setter
    private Dealership dealership;

    @ManyToOne
    @JoinColumn(name = "fk_supplier_id")
    @JsonIgnoreProperties("shippingContracts")
    @Getter
    @Setter
    private Supplier supplier;

    @ManyToOne()
    @JoinColumn(name = "fk_author_id")
    @JsonIgnoreProperties({"cars", "employees", "shippingContracts", "dealerships", "suppliers"})
    @Getter
    @Setter
    private AppUser author;
}
