package com.example.collab_space.requestDto;

import com.example.collab_space.Enums.Role;
import lombok.Data;

@Data
public class InviteUserDto {
    String adminEmail;
    String userEmail;
    Role userRole;
}
