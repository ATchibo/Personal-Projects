package com.example.assignment1_1.dtos;

import com.example.assignment1_1.domain.Dealership;
import com.example.assignment1_1.domain.Supplier;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
public class ContractDTO {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Date contractDate;

    @Getter
    @Setter
    private Integer contractYearsDuration;

    @Getter
    @Setter
    private Long dealershipId;

    @Getter
    @Setter
    private String dealershipName;

    @Getter
    @Setter
    private Long supplierId;

    @Getter
    @Setter
    private String supplierName;

    @Getter
    @Setter
    private Long authorId;

    @Getter
    @Setter
    private String authorUsername;
}
