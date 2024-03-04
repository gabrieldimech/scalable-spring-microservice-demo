package com.example.demo.repository;

import com.example.demo.model.WalletTransaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface WalletTransactionRepository extends ReactiveMongoRepository<WalletTransaction, String> {
    Flux<WalletTransaction> findByPlayerId(String playerId);
}
