package com.example.voting.models;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class VotingResponse {
    private String title;
    private String description;
}
