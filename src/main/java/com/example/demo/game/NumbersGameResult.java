package com.example.demo.game;

import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
//@Data
//@Builder
public class NumbersGameResult extends GameResult {
    private final int betRequestNumber;
    private final int betResultNumber;

    NumbersGameResult(BigDecimal betAmount, BigDecimal winAmount, int betRequestNumber, int betResultNumber) {
        super(betAmount, winAmount);
        this.betRequestNumber = betRequestNumber;
        this.betResultNumber = betResultNumber;
    }
}
