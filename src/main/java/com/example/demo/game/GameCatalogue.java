package com.example.demo.game;

import lombok.Getter;

@Getter
public enum GameCatalogue {
    NUMBERS_GAME(new NumbersGame());

    private final Game game;

    GameCatalogue(Game game) {
        this.game = game;
    }
}
