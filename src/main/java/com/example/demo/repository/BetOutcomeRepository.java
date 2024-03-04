package com.example.demo.repository;

import com.example.demo.model.BetOutcome;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BetOutcomeRepository extends ReactiveMongoRepository<BetOutcome, String> {
}
