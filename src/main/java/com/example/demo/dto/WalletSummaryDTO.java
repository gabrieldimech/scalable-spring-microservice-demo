package com.example.demo.dto;

import com.example.demo.model.Currency;
import com.example.demo.model.WalletTransaction;

import java.math.BigDecimal;
import java.util.List;

public record WalletSummaryDTO(String walletId, String playerId, BigDecimal balance, Currency currency, List<WalletTransaction> walletTransactions) {
}
