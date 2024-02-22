package com.example.demo.service;

import com.example.demo.game.BetRequestDTO;
import com.example.demo.game.GameResult;

public interface GameService {
    GameResult playGame(BetRequestDTO betRequestDTO, String gameName);
}
