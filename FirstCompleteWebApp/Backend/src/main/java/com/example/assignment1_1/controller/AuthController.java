package com.example.assignment1_1.controller;


import com.example.assignment1_1.config.UserAuthProvider;
import com.example.assignment1_1.domain.AppUser;
import com.example.assignment1_1.domain.RefreshToken;
import com.example.assignment1_1.domain.TokenRefreshRequest;
import com.example.assignment1_1.domain.TokenRefreshResponse;
import com.example.assignment1_1.dtos.CredentialsDto;
import com.example.assignment1_1.dtos.SignUpDto;
import com.example.assignment1_1.dtos.UserDto;
import com.example.assignment1_1.exceptions.TokenRefreshException;
import com.example.assignment1_1.service.RefreshTokenService;
import com.example.assignment1_1.service.UserConfirmationService;
import com.example.assignment1_1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;
    private final UserConfirmationService userConfirmationService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/api/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {

        System.out.println("Login attempt: " + credentialsDto.getUsername() + " " + credentialsDto.getPassword());

        UserDto user = userService.login(credentialsDto);

        user.setToken(userAuthProvider.createToken(user.getUsername()));
        user.setRefreshToken(refreshTokenService.createRefreshToken(user.getId()).getToken());

        return ResponseEntity.ok(user);
    }

    @PostMapping(path = "/api/register", produces = "application/string")
    public ResponseEntity<String> register(@RequestBody SignUpDto signUpDto) {
        UserDto user = userService.register(signUpDto);

        user.setToken(userAuthProvider.createToken(user.getUsername()));
        String confirmationCode = userConfirmationService.addUserConfirmation(userService.findUserById(user.getId()).getBody());

        return ResponseEntity.ok().body(confirmationCode);
    }

    @PostMapping(path = "/api/register/confirm/{token}", produces = "application/string")
    public ResponseEntity<String> confirm(@PathVariable String token) {
        AppUser user = userConfirmationService.confirmUser(token);
        return ResponseEntity.ok().body("User " + user.getUsername() + " has been confirmed");
    }

    @PostMapping(path ="/api/refreshtoken", produces = "application/json")
    public ResponseEntity<?> refreshtoken(@RequestBody Object request) {
        System.out.println("Refresh token request: " + request.toString());
        String requestRefreshToken = request.toString().split("=")[1].split("}")[0];
        System.out.println("Refresh token: " + requestRefreshToken);

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = userAuthProvider.createToken(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
