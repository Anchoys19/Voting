package com.example.voting.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@Table(name = "vote")
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    private String username;
    private Long candidateId;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long votingId;
}
