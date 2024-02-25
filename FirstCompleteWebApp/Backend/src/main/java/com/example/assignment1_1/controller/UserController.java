package com.example.assignment1_1.controller;

import com.example.assignment1_1.domain.AppUser;
import com.example.assignment1_1.dtos.UserDto;
import com.example.assignment1_1.service.UserService;
import jakarta.validation.Valid;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findAllUsers(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                          @RequestParam(value = "size", defaultValue = "25", required = false) int size) {
        return ResponseEntity.ok().body(userService.findAllUsers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> findUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping("/{id}/nr-contracts")
    public ResponseEntity<Long> countContractsForUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.countContractsForUser(id));
    }
    @GetMapping("/{id}/nr-dealerships")
    public ResponseEntity<Long> countDealershipsForUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.countDealershipsForUser(id));
    }
    @GetMapping("/{id}/nr-cars")
    public ResponseEntity<Long> countCarsForUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.countCarsForUser(id));
    }
    @GetMapping("/{id}/nr-employees")
    public ResponseEntity<Long> countEmployeesForUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.countEmployeesForUser(id));
    }
    @GetMapping("/{id}/nr-suppliers")
    public ResponseEntity<Long> countSuppliersForUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.countSuppliersForUser(id));
    }

    @GetMapping("/{id}/nr-entities-added")
    public ResponseEntity<?> countAllForUser(@PathVariable Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.appendField("dealerships", userService.countDealershipsForUser(id));
        jsonObject.appendField("contracts", userService.countContractsForUser(id));
        jsonObject.appendField("cars", userService.countCarsForUser(id));
        jsonObject.appendField("employees", userService.countEmployeesForUser(id));
        jsonObject.appendField("suppliers", userService.countSuppliersForUser(id));

        return ResponseEntity.ok().body(jsonObject);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppUser> saveUser(@Valid @RequestBody AppUser user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @Valid @RequestBody AppUser user) {
        return userService.updateUser(id, user);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long id, @RequestBody JSONObject role) {
        return ResponseEntity.ok().body(userService.updateUserRole(id, role.get("role").toString()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/page-size/{size}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> setPageSize(@PathVariable Long size) {
        userService.setPageSize(size);
        return ResponseEntity.ok().build();
    }
}
