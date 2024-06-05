package com.example.voting.services;

import com.example.voting.models.Candidate;
import com.example.voting.models.Voting;
import com.example.voting.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public List<Candidate> getCandidatesByVoting(Voting voting) {
        return candidateRepository.findByVoting(voting);
    }

    public void createCandidate(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    public void changeCandidateActivity(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        candidate.setActive(true);
        candidateRepository.save(candidate);
    }
}

