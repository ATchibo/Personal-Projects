package com.example.assignment1_1.config;

import com.example.assignment1_1.domain.AppUser;
import com.example.assignment1_1.dtos.SignUpDto;
import com.example.assignment1_1.mappers.UserMapper;
import com.example.assignment1_1.repo.UserRepo;
import com.example.assignment1_1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (userRepository.count() == 0) {

            AppUser user1 = new AppUser();
            user1.setId(1L);
            user1.setFirstName("Andrei");
            user1.setLastName("Cibo");
            user1.setPassword(passwordEncoder.encode(CharBuffer.wrap("password")));
            user1.setEmail("e@mail.com");
            user1.setRole("ROLE_ADMIN");
            user1.setLocation("Suceava");
            user1.setUsername("tchibo");
            user1.setAccountStatus("ACTIVE");

            userRepository.save(user1);
        }
    }
}
