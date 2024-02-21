package com.example.demo.game;

import java.util.Random;

public class GameRNG {
    private static final Random random = new Random();

    public static int generateNumberInclusive(int minNumber, int maxNumber) {
        return random.nextInt(minNumber, maxNumber);
    }
}
