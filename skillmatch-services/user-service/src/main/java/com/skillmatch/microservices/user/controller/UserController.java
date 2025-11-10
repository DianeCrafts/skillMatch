package com.skillmatch.microservices.user.controller;

import com.skillmatch.microservices.user.dto.*;
import com.skillmatch.microservices.user.mapper.UserMapper;
import com.skillmatch.microservices.user.model.User;
import com.skillmatch.microservices.user.security.JwtService;
import com.skillmatch.microservices.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .build();

        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(UserMapper.toResponse(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        boolean ok = userService.loginUser(req.getEmail(), req.getPassword());
        if (!ok) {
            return ResponseEntity.badRequest().build();
        }

        String role = userService.findByEmail(req.getEmail())
                .map(user -> user.getRole().name())
                .orElse("USER");

        String token = jwtService.generateToken(req.getEmail(), Map.of("role", role));
        return ResponseEntity.ok(new AuthResponse(token));
    }


    // Get all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return (user != null)
                ? ResponseEntity.ok(UserMapper.toResponse(user))
                : ResponseEntity.notFound().build();
    }




    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {

        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setRole(request.getRole());

        User updatedUser = userService.saveUser(existingUser);
        return ResponseEntity.ok(UserMapper.toResponse(updatedUser));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
