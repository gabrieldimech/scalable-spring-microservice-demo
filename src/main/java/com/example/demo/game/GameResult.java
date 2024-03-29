package com.example.demo.game;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
public abstract class GameResult {
    private final BigDecimal betAmount;
    private final BigDecimal winAmount;
}
