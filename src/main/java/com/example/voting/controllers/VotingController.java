package com.example.voting.controllers;

import com.example.voting.models.Candidate;
import com.example.voting.models.Vote;
import com.example.voting.models.Voting;
import com.example.voting.models.VotingResponse;
import com.example.voting.repository.CandidateRepository;
import com.example.voting.repository.UserRepository;
import com.example.voting.repository.VoteRepository;
import com.example.voting.repository.VotingRepository;
import com.example.voting.services.CandidateService;
import com.example.voting.services.VotingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/voting")
public class VotingController {

    private final VotingService votingService;
    private final VotingRepository votingRepository;
    private final VoteRepository voteRepository;

    private final CandidateRepository candidateRepository;
    private final CandidateService candidateService;


    public VotingController(VotingService votingService, VotingRepository votingRepository, VoteRepository voteRepository, CandidateRepository candidateRepository, CandidateService candidateService1) {
        this.votingService = votingService;
        this.votingRepository = votingRepository;
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
        this.candidateService = candidateService1;
    }


    @GetMapping("/add")
    public String showAddVotingPage(Model model) {
        model.addAttribute("newVoting", new Voting());
        return "voting/voting-add";
    }

    @PostMapping("/create")
    public String createVoting(@ModelAttribute("newVoting") VotingResponse votingResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        var voting = Voting.builder()
                .title(votingResponse.getTitle())
                .description(votingResponse.getDescription())
                .ownerUsername(username)
                .voteCount(0)
                .isActive(true)
                .build();
        votingRepository.save(voting);
        return "redirect:/";
    }

    @GetMapping("/{votingId}")
    public String showVoting(@PathVariable("votingId") Long votingId, Model model) {
        Voting voting = votingService.getVotingById(votingId);
        List<Candidate> candidates = candidateService.getCandidatesByVoting(voting);
        model.addAttribute("voting", voting);
        model.addAttribute("candidates", candidates);
        model.addAttribute("newCandidate", new Candidate());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username.equals(voting.getOwnerUsername())) {
            model.addAttribute("isOwner", true);
        } else {
            model.addAttribute("isOwner", false);
        }
        boolean hasVoted = voteRepository.existsByUsernameAndVotingId(username, votingId);
        model.addAttribute("hasVoted", hasVoted);


        return "voting/voting-info";
    }

    @PostMapping("/{votingId}/candidates/create")
    public String addCandidateToVoting(
            @PathVariable("votingId") Long votingId,
            @ModelAttribute("newCandidate") Candidate newCandidate
    ) {
        Voting voting = votingService.getVotingById(votingId);
        candidateService.createCandidate(newCandidate);
        Candidate candidate = newCandidate;
        candidate.setVoting(voting);
        candidate.setActive(false);
        candidateRepository.save(candidate);
        votingService.saveVoting(voting);
        return "redirect:/voting/{votingId}";
    }

    @PostMapping("/{votingId}/choose/{candidateId}")
    public String chooseCandidate(@PathVariable Long votingId, @PathVariable Long candidateId) {
        candidateService.changeCandidateActivity(candidateId);
        return "redirect:/voting/{votingId}";
    }

    @PostMapping("/{votingId}/vote")
    public String voteForCandidate(
            @PathVariable("votingId") Long votingId,
            @RequestParam("selectedCandidate") Long candidateId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        var vote = Vote.builder()
                .username(username)
                .candidateId(candidateId)
                .votingId(votingId)
                .build();
        voteRepository.save(vote);

        Optional<Voting> votingOptional = votingRepository.findById(votingId);
        if (votingOptional.isPresent()) {
            Voting voting = votingOptional.get();
            int currentVoteCount = voting.getVoteCount();

            int newVoteCount = currentVoteCount + 1;
            voting.setVoteCount(newVoteCount);
            votingRepository.save(voting);
        } else {
        }

        return "redirect:/voting/{votingId}";
    }

    @PostMapping("/{votingId}/changeActiveFalse")
    public String changeActiveFalse(@PathVariable Long votingId) {
        votingService.changeVotingStatusFalse(votingId);
        return "redirect:/voting/{votingId}";
    }

    @PostMapping("/{votingId}/changeActiveTrue")
    public String changeActiveTrue(@PathVariable Long votingId) {
        votingService.changeVotingStatusTrue(votingId);
        return "redirect:/voting/{votingId}";
    }

    @GetMapping("/{votingId}/results")
    public String showResults(@PathVariable("votingId") Long votingId, Model model) {
        Voting voting = votingService.getVotingById(votingId);

        List<Candidate> candidates = voting.getCandidates();

        Map<Long, Integer> votes = votingService.getVotesByVotingId(votingId);

        int totalCount = voteRepository.countByVotingId(votingId);

        model.addAttribute("voting", voting);
        model.addAttribute("candidates", candidates);
        model.addAttribute("votes", votes);
        model.addAttribute("totalCount", totalCount);

        return "voting/voting-result";

    }
}



