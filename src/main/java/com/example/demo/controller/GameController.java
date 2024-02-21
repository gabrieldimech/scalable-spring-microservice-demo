package com.example.demo.controller;

import com.example.demo.dto.LeaderBoardDTO;
import com.example.demo.game.BetRequestDTO;
import com.example.demo.game.GameResult;
import com.example.demo.service.GameService;
import com.example.demo.service.LeaderBoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    private final GameService gameService;
    private final LeaderBoardService leaderBoardService;

    public GameController(GameService gameService, LeaderBoardService leaderBoardService) {
        this.gameService = gameService;
        this.leaderBoardService = leaderBoardService;
    }

    @PostMapping("/games/{gameName}")
    ResponseEntity<?> playGame(@RequestBody BetRequestDTO betRequestDTO, @PathVariable String gameName) {
        final GameResult gameResult = gameService.playGame(betRequestDTO, gameName);
        return new ResponseEntity<>(gameResult, HttpStatus.OK);
    }

    @GetMapping("/leaderBoard")
    ResponseEntity<?> getLeaderBoard(){
        final LeaderBoardDTO leaderBoardDTOResponse = leaderBoardService.getLeaderBoard();
        return new ResponseEntity<>(leaderBoardDTOResponse, HttpStatus.OK);
    }

}
