package com.example.demo.repository;

import com.example.demo.model.Player;
import com.example.demo.model.WalletTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WalletTransactionRepository extends MongoRepository<WalletTransaction, String> {
    List<WalletTransaction> findByPlayerId(String playerId);
}
