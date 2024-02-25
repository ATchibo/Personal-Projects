package com.example.assignment1_1.repo;

import com.example.assignment1_1.domain.GlobalPageSize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageSizeRepo extends JpaRepository<GlobalPageSize, Long> {
}
