package com.example.demo.game;

import java.math.BigDecimal;

public interface Game {
    void validateBetRequest(BetRequestDTO betRequestDTO);
    NumbersGameResult playGameRound(BigDecimal betAmount, int betNumber);
}
