package com.example.ecommerce.dto;

import com.example.ecommerce.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDTO {
    private String name;
    private String email;
    private String password;
    private Role role;
}
