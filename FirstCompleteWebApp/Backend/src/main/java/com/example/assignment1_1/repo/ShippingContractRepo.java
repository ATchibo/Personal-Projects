package com.example.assignment1_1.repo;

import com.example.assignment1_1.domain.ShippingContract;
import com.example.assignment1_1.dtos.ContractDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingContractRepo extends JpaRepository<ShippingContract, Long> {}
