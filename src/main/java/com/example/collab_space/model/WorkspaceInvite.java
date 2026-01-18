package com.example.collab_space.model;

import com.example.collab_space.Enums.InviteStatus;
import com.example.collab_space.Enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class WorkspaceInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Workspace workspace;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Role userRole;

    @Column(nullable = false)
    LocalDateTime invitedAt;

    @Column(nullable = false)
    LocalDateTime expiresAt;

    @Column(nullable = false)
    UUID inviteToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    InviteStatus inviteStatus;

}
