package com.example.voting.models;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RegisterFormResponse {
    @Size(min = 3, message = "Minimal username length is 3 characters")
    private String username;
    @Size(min = 5, message = "Minimal password length is 5 characters")
    private String password;
    @Size(min = 5, message = "Minimal password length is 5 characters")
    private String confirmPassword;
}
