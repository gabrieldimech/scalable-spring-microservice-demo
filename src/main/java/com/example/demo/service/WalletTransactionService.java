package com.example.demo.service;

import com.example.demo.model.WalletTransaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public interface WalletTransactionService {
     void saveBetAndUpdateBalanceTransactions(String betReferenceId, BigDecimal betAmount, BigDecimal winAmount, String playerId);
     Mono<WalletTransaction> saveDepositTransaction(String walletId, String playerId, BigDecimal amount, String referenceId);

     Flux<WalletTransaction> findByPlayerId(String playerId);
}
