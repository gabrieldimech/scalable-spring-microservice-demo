package com.example.demo.service;

import com.example.demo.model.BetOutcome;
import com.example.demo.repository.BetOutcomeRepository;
import org.springframework.stereotype.Service;

@Service
public class BetOutcomeServiceImpl implements BetOutcomeService {
    private final BetOutcomeRepository betOutcomeRepository;

    public BetOutcomeServiceImpl(BetOutcomeRepository betOutcomeRepository) {
        this.betOutcomeRepository = betOutcomeRepository;
    }

    @Override
    public void saveBetOutcome(BetOutcome betOutcome) {
        betOutcomeRepository.save(betOutcome);
    }
}
