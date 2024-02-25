package com.example.assignment1_1.repo;

import com.example.assignment1_1.domain.Dealership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealershipRepo extends JpaRepository<Dealership, Long>, JpaSpecificationExecutor<Dealership> {

    Optional<Dealership> findById(Long id);
}
