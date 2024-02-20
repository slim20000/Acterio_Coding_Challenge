package com.slim.acterio.services;

import com.slim.acterio.dto.UserUpdateRequest;
import com.slim.acterio.models.User;
import com.slim.acterio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User registerUser(String email, String password, String firstName, String lastName) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return userRepository.save(user);
    }


    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        return passwordEncoder.matches(password, user.getPassword());
    }

    public Map<String, Integer> getDomainOccurrences() {
        List<User> userList = userRepository.findAll();
        Map<String, Integer> domainOccurrences = new HashMap<>();
        for (User user : userList) {
            String email = user.getEmail();
            String domain = extractDomain(email);
            domainOccurrences.put(domain, domainOccurrences.getOrDefault(domain, 0) + 1);
        }
        return domainOccurrences;
    }

    private String extractDomain(String email) {
        Pattern pattern = Pattern.compile("@(.+)$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalArgumentException("Invalid email");
        }
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public void deleteUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        } else {
            throw new IllegalArgumentException("Dont exist");
        }
    }
    public User updateUser(Long userId, String firstName, String lastName, String email, String password) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    public User updateUser(Long userId, UserUpdateRequest request) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (request.getFirstName() != null) {
                user.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                user.setLastName(request.getLastName());
            }
            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

}