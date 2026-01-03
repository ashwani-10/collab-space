package com.example.collab_space.model;

import com.example.collab_space.Enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;
    @Column(nullable = false)
    String name;
    String imageUrl;
    @Column(nullable = false)
    String email;
    @Column(nullable = false)
    String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Role role;

    @CreationTimestamp
    LocalDate createdAt;
}
