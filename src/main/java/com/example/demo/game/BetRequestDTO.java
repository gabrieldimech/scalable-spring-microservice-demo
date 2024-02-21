package com.example.demo.game;

import java.math.BigDecimal;

public record BetRequestDTO(String betReferenceId, String playerId, BigDecimal betAmount, int betNumber) {
}
