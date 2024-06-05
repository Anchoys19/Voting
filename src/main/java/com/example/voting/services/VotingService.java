package com.example.voting.services;

import com.example.voting.models.Candidate;
import com.example.voting.models.Voting;
import com.example.voting.repository.CandidateRepository;
import com.example.voting.repository.VoteRepository;
import com.example.voting.repository.VotingRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VotingService {

    private final VotingRepository votingRepository;
    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;

    public VotingService(VotingRepository votingRepository, VoteRepository voteRepository, CandidateRepository candidateRepository) {
        this.votingRepository = votingRepository;
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
    }

    public void saveVoting(Voting voting) {
        votingRepository.save(voting);
    }

    public Voting getVotingById(Long votingId) {
        return votingRepository.findById(votingId).orElse(null);
    }

    public List<Voting> getAllVotings() {
        return votingRepository.findAll();
    }

    public void changeVotingStatusFalse(Long votingId) {
        Voting voting = votingRepository.findById(votingId).orElse(null);
        voting.setActive(false);
        votingRepository.save(voting);
    }

    public void changeVotingStatusTrue(Long votingId) {
        Voting voting = votingRepository.findById(votingId).orElse(null);
        voting.setActive(true);
        votingRepository.save(voting);
    }

    public Map<Long, Integer> getVotesByVotingId(Long votingId) {

        List<Candidate> candidates = candidateRepository.findCandidatesByVotingId(votingId);

        Map<Long, Integer> votes = new HashMap<>();

        for (Candidate candidate : candidates) {
            int voteCount = voteRepository.countByCandidateId(candidate.getId());
            votes.put(candidate.getId(), voteCount);
        }

        return votes;
    }

}
