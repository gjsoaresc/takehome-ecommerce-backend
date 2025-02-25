package com.example.ecommerce.dto;

import com.example.ecommerce.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private String username;
    private String password;
    private Role role;
}
