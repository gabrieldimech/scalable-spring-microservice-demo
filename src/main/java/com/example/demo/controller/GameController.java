package com.example.demo.controller;

import com.example.demo.dto.LeaderBoardDTO;
import com.example.demo.game.BetRequestDTO;
import com.example.demo.game.GameResult;
import com.example.demo.service.GameService;
import com.example.demo.service.LeaderBoardServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class GameController {

    private final GameService gameService;
    private final LeaderBoardServiceImpl leaderBoardService;

    public GameController(GameService gameService, LeaderBoardServiceImpl leaderBoardService) {
        this.gameService = gameService;
        this.leaderBoardService = leaderBoardService;
    }

    @PostMapping("/games/{gameName}")
    public ResponseEntity<?> playGame(@RequestBody BetRequestDTO betRequestDTO, @PathVariable String gameName) {
        final GameResult gameResult = gameService.playGame(betRequestDTO, gameName);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("playerId",
                betRequestDTO.playerId());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(gameResult);
    }

    @GetMapping("/leaderBoard")
    public Mono<LeaderBoardDTO> getLeaderBoard() {
        return leaderBoardService.getLeaderBoard();
    }

}
