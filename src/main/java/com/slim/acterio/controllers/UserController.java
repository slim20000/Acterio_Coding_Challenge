package com.slim.acterio.controllers;

import com.slim.acterio.dto.RegistrationRequest;
import com.slim.acterio.dto.UserUpdateRequest;
import com.slim.acterio.models.User;
import com.slim.acterio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegistrationRequest request) {
        try {
            User newUser = userService.registerUser(request.getEmail(), request.getPassword(), request.getFirstName(), request.getLastName());
            Map<String, String> response = Map.of("message", "User registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            String errorMessage = String.format("This email '%s' exists.", request.getEmail());
            Map<String, String> response = Map.of("error", errorMessage);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestParam String email, @RequestParam String password) {
        boolean authenticated = userService.authenticateUser(email, password);
        if (authenticated) {
            Map<String, String> response = Map.of("message", "Login successful");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = Map.of("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/domain-occurrences")
    public ResponseEntity<Map<String, Integer>> getDomainOccurrences() {
        Map<String, Integer> domainOccurrences = userService.getDomainOccurrences();
        return ResponseEntity.ok(domainOccurrences);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        return userOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId,
                                           @RequestParam(required = false) String firstName,
                                           @RequestParam(required = false) String lastName,
                                           @RequestParam(required = false) String email,
                                           @RequestParam(required = false) String password) {
        try {
            User updatedUser = userService.updateUser(userId, firstName, lastName, email, password);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        try {
            User updatedUser = userService.updateUser(userId, request);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}