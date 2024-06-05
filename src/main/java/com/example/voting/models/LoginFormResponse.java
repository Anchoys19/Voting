package com.example.voting.models;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LoginFormResponse {
    private String username;
    private String password;
}
