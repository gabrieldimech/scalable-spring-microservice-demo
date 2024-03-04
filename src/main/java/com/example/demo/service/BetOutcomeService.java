package com.example.demo.service;

import com.example.demo.model.BetOutcome;
import reactor.core.publisher.Mono;

public interface BetOutcomeService {
    Mono<BetOutcome> saveBetOutcome(BetOutcome betOutcome);
    Mono<BetOutcome> findById(String uuid);
}
