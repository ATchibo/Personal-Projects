package com.example.assignment1_1.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.assignment1_1.domain.AccountStatus;
import com.example.assignment1_1.domain.AppUser;
import com.example.assignment1_1.domain.GlobalPageSize;
import com.example.assignment1_1.domain.Role;
import com.example.assignment1_1.dtos.CredentialsDto;
import com.example.assignment1_1.dtos.SignUpDto;
import com.example.assignment1_1.dtos.UserDto;
import com.example.assignment1_1.exceptions.AppException;
import com.example.assignment1_1.mappers.UserMapper;
import com.example.assignment1_1.repo.MyUserRepo;
import com.example.assignment1_1.repo.PageSizeRepo;
import com.example.assignment1_1.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private MyUserRepo myUserRepo;

    @Autowired
    private PageSizeRepo pageSizeRepo;

    private UserMapper userMapper = UserMapper.INSTANCE;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<AppUser> findUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    public UserDto findByUsername(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return userMapper.toUserDto(user);
    }

    public AppUser findByUsernameFull(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<AppUser> saveUser(AppUser user) {
        try {
            AppUser newUser = userRepository.save(user);
            return ResponseEntity.ok().body(newUser);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<AppUser> updateUser(Long id, AppUser user) {
        return userRepository.findById(id)
                .map(userToUpdate -> {
                    userToUpdate.setPassword(user.getPassword());
                    userToUpdate.setFirstName(user.getFirstName());
                    userToUpdate.setLastName(user.getLastName());
                    userToUpdate.setRole(user.getRole());

                    AppUser updatedUser = userRepository.save(userToUpdate);
                    return ResponseEntity.ok().body(updatedUser);
                }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<AppUser> deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(id);
                    return ResponseEntity.ok().body(user);
                }).orElse(ResponseEntity.notFound().build());
    }

    public Long countContractsForUser(Long id) {
        return myUserRepo.countContractsForUser(id);
    }

    public Long countDealershipsForUser(Long id) {
        return myUserRepo.countDealershipsForUser(id);
    }

    public Long countCarsForUser(Long id) {
        return myUserRepo.countCarsForUser(id);
    }

    public Long countEmployeesForUser(Long id) {
        return myUserRepo.countEmployeesForUser(id);
    }

    public Long countSuppliersForUser(Long id) {
        return myUserRepo.countSuppliersForUser(id);
    }

    public UserDto login(CredentialsDto credentialsDto) {
        AppUser user = userRepository.findByUsername(credentialsDto.getUsername())
                .orElseThrow(() -> new AppException("Unknown user: " + credentialsDto.getUsername(), HttpStatus.NOT_FOUND));

        if (!Objects.equals(user.getAccountStatus(), AccountStatus.ACTIVE.toString())) {
            throw new AppException("Account is not activated", HttpStatus.FORBIDDEN);
        }

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }

        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto) {
        Optional<AppUser> optionalAppUser = userRepository.findByUsername(userDto.getUsername());

        if (optionalAppUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        AppUser user = userMapper.signUpToUser(userDto);

        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));

        try {
            AppUser savedUser = userRepository.save(user);
            return userMapper.toUserDto(savedUser);
        } catch (Exception e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public List<UserDto> findAllUsers(int page, int size) {
        if (!pageSizeRepo.findAll().isEmpty()) {
            size = pageSizeRepo.findAll().get(0).getPageSize().intValue();
        }

        Pageable pageable = Pageable.ofSize(size).withPage(page);

        return myUserRepo.findAllUsers(pageable);
    }

    public ResponseEntity<UserDto> findUserByUsername(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok().body(userMapper.toUserDto(user));
    }

    public UserDto updateUserRole(Long id, String role) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (!isValidRole(role)) {
            throw new AppException("Invalid role", HttpStatus.BAD_REQUEST);
        }

        user.setRole(role);

        AppUser updatedUser = userRepository.save(user);

        return userMapper.toUserDto(updatedUser);
    }

    private boolean isValidRole(String roleString) {
        System.out.println(roleString);
        for (Role role : Role.values()) {
            System.out.println(role.name());

            if (role.name().equals(roleString)) {
                return true;
            }
        }
        return false;
    }

    public UserDto getCurrentUser() {
        try {
            return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    public void setPageSize(Long pageSize) {
        pageSizeRepo.deleteAll();
        pageSizeRepo.save(new GlobalPageSize(pageSize));
    }
}
