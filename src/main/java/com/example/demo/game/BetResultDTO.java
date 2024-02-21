package com.example.demo.game;

import java.math.BigDecimal;

public record BetResultDTO(String betReferenceId, String playerId, BigDecimal betAmount, BigDecimal winAmount,
                           int betRequestNumber, int betResultNumber) {
}
