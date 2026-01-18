package com.example.collab_space.model;

import com.example.collab_space.Enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "workspaceMember")
public class WorkspaceMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Workspace workspace;

    @ManyToOne
    User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Role role;

    @Column(nullable = false)
    boolean isActiveInWorkspace;

    @CreationTimestamp
    LocalDate joinedAt;

}
