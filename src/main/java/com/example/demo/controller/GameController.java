package com.example.demo.controller;

import com.example.demo.dto.LeaderBoardDTO;
import com.example.demo.game.BetRequestDTO;
import com.example.demo.game.GameResult;
import com.example.demo.service.GameService;
import com.example.demo.service.LeaderBoardService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
public class GameController {

    private final GameService gameService;
    private final LeaderBoardService leaderBoardService;

    public GameController(GameService gameService, LeaderBoardService leaderBoardService) {
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
    @Async
    public CompletableFuture<?> getLeaderBoard() {
        final LeaderBoardDTO leaderBoardDTOResponse = leaderBoardService.getLeaderBoard();
        return CompletableFuture.completedFuture(new ResponseEntity<>(leaderBoardDTOResponse, HttpStatus.OK));
    }

}
