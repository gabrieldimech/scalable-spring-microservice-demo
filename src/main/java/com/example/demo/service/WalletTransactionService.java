package com.example.demo.service;

import com.example.demo.model.WalletTransaction;

import java.math.BigDecimal;
import java.util.List;

public interface WalletTransactionService {
     void saveBetAndUpdateBalanceTransactions(String betReferenceId, BigDecimal betAmount, BigDecimal winAmount, String playerId);
     void saveDepositTransaction(String walletId, String playerId, BigDecimal amount, String referenceId);

     List<WalletTransaction> findByPlayerId(String playerId);
}
