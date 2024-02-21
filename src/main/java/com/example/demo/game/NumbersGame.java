package com.example.demo.game;

import com.example.demo.exception.InvalidBetNumberException;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.MessageFormat;

import static java.lang.Math.abs;

public class NumbersGame implements Game {
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 10;
    private static final BigDecimal CORRECT_NUMBER = BigDecimal.valueOf(10);
    private static final BigDecimal OFF_BY_ONE = BigDecimal.valueOf(5);
    private static final BigDecimal OFF_BY_TWO = BigDecimal.valueOf(0.5);
    Logger logger = LoggerFactory.getLogger(NumbersGame.class);

    @Override
    public void validateBetRequest(BetRequestDTO betRequestDTO) {
        if (betRequestDTO.betNumber() < MIN_NUMBER || betRequestDTO.betNumber() > MAX_NUMBER) {
            String message = MessageFormat.format("Invalid bet number {0}, should be between {1} and {2}}", betRequestDTO.betNumber(), MIN_NUMBER, MAX_NUMBER);
            logger.error(message);
            throw new InvalidBetNumberException(message);
        }
    }

    @Override
    public NumbersGameResult playGameRound(BigDecimal betAmount, int betNumber) {
        val randomNumber = GameRNG.generateNumberInclusive(MIN_NUMBER, MAX_NUMBER);
        val diff = abs(randomNumber - betNumber);

        val winAmount = switch (diff) {
            case 0 -> betAmount.multiply(CORRECT_NUMBER);
            case 1 -> betAmount.multiply(OFF_BY_ONE);
            case 2 -> betAmount.multiply(OFF_BY_TWO);
            default -> BigDecimal.ZERO;
        };

        return new NumbersGameResult(betAmount, winAmount, betNumber, randomNumber);
    }
}
