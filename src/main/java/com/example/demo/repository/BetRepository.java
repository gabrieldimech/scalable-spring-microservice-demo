package com.example.demo.repository;

import com.example.demo.model.Bet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BetRepository extends MongoRepository<Bet, String> {
}
