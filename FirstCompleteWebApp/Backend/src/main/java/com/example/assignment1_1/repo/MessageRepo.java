package com.example.assignment1_1.repo;

import com.example.assignment1_1.domain.Employee;
import com.example.assignment1_1.domain.TextMessage;
import com.example.assignment1_1.dtos.TextMessageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<TextMessage, Long> {}