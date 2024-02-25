package com.example.assignment1_1.repo;

import com.example.assignment1_1.domain.UserConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConfirmationRepo extends JpaRepository<UserConfirmation, Long> {

    UserConfirmation findByToken(String token);
}
