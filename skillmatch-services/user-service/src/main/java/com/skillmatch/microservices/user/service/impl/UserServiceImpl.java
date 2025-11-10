//package com.skillmatch.microservices.user.service.impl;
//
//import com.skillmatch.microservices.user.dto.LoginRequest;
//import com.skillmatch.microservices.user.dto.RegisterRequest;
//import com.skillmatch.microservices.user.dto.UpdateUserRequest;
//import com.skillmatch.microservices.user.exception.NotFoundException;
//import com.skillmatch.microservices.user.model.User;
//import com.skillmatch.microservices.user.repository.UserRepository;
//import com.skillmatch.microservices.user.security.JwtUtil;
//import com.skillmatch.microservices.user.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * Implements the business logic for User operations.
// * Handles registration, authentication, and user profile management.
// */
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository repo;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtil jwtUtil;
//
//    // 🔹 Register a new user
//    @Override
//    public User register(RegisterRequest request) {
//        if (repo.existsByEmail(request.getEmail())) {
//            throw new IllegalArgumentException("Email already registered");
//        }
//
//        User user = User.builder()
//                .name(request.getName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword())) // hash password
//                .role(request.getRole())
//                .build();
//
//        return repo.save(user);
//    }
//
//    // 🔹 Login and return JWT token
//    @Override
//    public String login(LoginRequest request) {
//        User user = repo.findByEmail(request.getEmail())
//                .orElseThrow(() -> new NotFoundException("User not found with email: " + request.getEmail()));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("Invalid credentials");
//        }
//
//        // Generate JWT token
//        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
//    }
//
//    // 🔹 Get all users
//    @Override
//    public List<User> getAll() {
//        return repo.findAll();
//    }
//
//    // 🔹 Get user by ID
//    @Override
//    public User getById(Long id) {
//        return repo.findById(id)
//                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
//    }
//
//    // 🔹 Get user by Email
//    @Override
//    public User getByEmail(String email) {
//        return repo.findByEmail(email)
//                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
//    }
//
//    // 🔹 Update user details
//    @Override
//    public User update(Long id, UpdateUserRequest request) {
//        User existing = getById(id);
//        existing.setName(request.getName());
//        existing.setEmail(request.getEmail());
//        existing.setRole(request.getRole());
//        return repo.save(existing);
//    }
//
//    // 🔹 Delete a user
//    @Override
//    public void delete(Long id) {
//        if (!repo.existsById(id)) {
//            throw new NotFoundException("User not found with id: " + id);
//        }
//        repo.deleteById(id);
//    }
//
//    // 🔹 Change password
//    @Override
//    public void changePassword(Long id, String oldPassword, String newPassword) {
//        User user = getById(id);
//
//        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
//            throw new IllegalArgumentException("Old password is incorrect");
//        }
//
//        user.setPassword(passwordEncoder.encode(newPassword));
//        repo.save(user);
//    }
//
//    // 🔹 Get user from JWT token
//    @Override
//    public User currentUserFromToken(String bearerToken) {
//        String token = bearerToken.replace("Bearer ", "");
//        String email = jwtUtil.extractUsername(token);
//        return getByEmail(email);
//    }
//}
