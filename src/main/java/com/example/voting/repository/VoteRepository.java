package com.example.voting.repository;

import com.example.voting.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findById(Long id);
    boolean existsByUsernameAndVotingId(String username, Long votingId);

    int countByCandidateId(Long candidateId);

    int countByVotingId(Long votingId);
}
