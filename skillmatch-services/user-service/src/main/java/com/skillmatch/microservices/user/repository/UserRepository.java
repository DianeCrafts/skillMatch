package com.skillmatch.microservices.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skillmatch.microservices.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}