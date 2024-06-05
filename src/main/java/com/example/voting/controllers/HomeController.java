package com.example.voting.controllers;

import com.example.voting.models.Voting;
import com.example.voting.services.VotingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final VotingService votingService;

    public HomeController(VotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping("/")
    public String voting(Model model) {
        List<Voting> votingList = votingService.getAllVotings();
        model.addAttribute("votingList", votingList);
        return "voting/index";
    }
}
