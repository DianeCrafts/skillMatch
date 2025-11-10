package com.skillmatch.microservices.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.OffsetDateTime;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 100)
    private String name;


    @Column(nullable = false, unique = true, length = 150)
    private String email;


    @Column(nullable = false, length = 255)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;


    @CreationTimestamp
    private OffsetDateTime createdAt;


    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}