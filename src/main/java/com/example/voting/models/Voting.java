package com.example.voting.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "voting")
@Data
@Builder
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Voting {
    private String title;
    private String description;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // id опитування
    private int voteCount;
    private String ownerUsername;
    private boolean isActive;
    @OneToMany(mappedBy = "voting")
    private List<Candidate> candidates;
    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}

