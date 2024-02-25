package com.example.assignment1_1.service;

import com.example.assignment1_1.domain.AccountStatus;
import com.example.assignment1_1.domain.AppUser;
import com.example.assignment1_1.domain.UserConfirmation;
import com.example.assignment1_1.exceptions.AppException;
import com.example.assignment1_1.repo.UserConfirmationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserConfirmationService {

    @Autowired
    private UserConfirmationRepo userConfirmationRepo;

    @Autowired
    private UserService userService;

    public String addUserConfirmation(AppUser user) {
        UserConfirmation userConfirmation = new UserConfirmation();
        userConfirmation.setUser(user);
        userConfirmation.setToken(UUID.randomUUID().toString());
        userConfirmation.setExpirationDate(LocalDateTime.now().plusMinutes(10));

        saveUserConfirmation(userConfirmation);

        return userConfirmation.getToken();
    }

    public void saveUserConfirmation(UserConfirmation userConfirmation) {
        userConfirmationRepo.save(userConfirmation);
    }

    public AppUser confirmUser(String token) {
        UserConfirmation userConfirmation = userConfirmationRepo.findByToken(token);
        if (userConfirmation == null) {
            throw new AppException("Invalid token", HttpStatus.NOT_FOUND);
        }
        if (userConfirmation.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new AppException("Token expired", HttpStatus.BAD_REQUEST);
        }
        AppUser user = userConfirmation.getUser();
        user.setAccountStatus(AccountStatus.ACTIVE.toString());

        return userService.saveUser(user).getBody();
    }
}
