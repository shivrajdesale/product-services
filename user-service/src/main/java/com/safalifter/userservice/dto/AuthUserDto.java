package com.shivraj.userservice.dto;

import com.shivraj.userservice.enums.Role;
import lombok.Data;

@Data
public class AuthUserDto {
    private String id;
    private String username;
    private String password;
    private Role role;
}