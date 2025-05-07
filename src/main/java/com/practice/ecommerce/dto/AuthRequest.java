package com.practice.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {
    @Email(message = "Please use email format")
    private String username;
    @Size(min = 6, message = "Password length cannot be less than 6.")
    private String password;
}
