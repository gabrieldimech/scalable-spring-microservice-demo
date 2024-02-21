package com.example.demo.dto;

import com.example.demo.model.Currency;

import java.math.BigDecimal;

public record WalletDTO(String id, String playerId, BigDecimal balance, Currency currency) {
}
