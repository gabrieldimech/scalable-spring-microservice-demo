package com.example.demo.service;

import com.example.demo.model.BetOutcome;
import com.example.demo.repository.BetOutcomeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BetOutcomeServiceImpl implements BetOutcomeService {
    private final BetOutcomeRepository betOutcomeRepository;
    private final Logger logger = LoggerFactory.getLogger(BetOutcomeServiceImpl.class);


    public BetOutcomeServiceImpl(BetOutcomeRepository betOutcomeRepository) {
        this.betOutcomeRepository = betOutcomeRepository;
    }

    @Override
    public Mono<BetOutcome> saveBetOutcome(BetOutcome betOutcome) {
        return betOutcomeRepository.save(betOutcome);
    }

    @Override
    public Mono<BetOutcome> findById(String uuid) {
        return betOutcomeRepository.findById(uuid);
    }
}
