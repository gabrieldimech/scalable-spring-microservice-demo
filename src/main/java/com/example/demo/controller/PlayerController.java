package com.example.demo.controller;

import com.example.demo.dto.PlayerDTO;
import com.example.demo.dto.WalletSummaryDTO;
import com.example.demo.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class PlayerController {

    final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/player")
    Mono<ResponseEntity> createPlayer(@RequestBody PlayerDTO playerDTO) {
        Map<String, Object> map = new LinkedHashMap<>();
        return playerService.savePlayer(playerDTO)
                .map(playerDTOResult -> {
                    map.put("success", true);
                    map.put("message", "Player Added Successfully!");
                    map.put("player", playerDTOResult);
                    return ResponseEntity.status(HttpStatus.CREATED).body(map);
                });
    }

    @GetMapping("/player/{id}")
    Mono<PlayerDTO> getPlayer(@PathVariable String id) {
        return playerService.findPlayer(id);
    }

    @PutMapping("/player/{id}")
    ResponseEntity<?> updatePlayer(@PathVariable String id, @RequestBody PlayerDTO playerDTO) {
        Map<String, Object> map = new LinkedHashMap<>();
        Mono<PlayerDTO> playerDTOResult = playerService.updatePlayer(id, playerDTO);

        map.put("success", true);
        map.put("message", "Player Updated Successfully!");
        map.put("player", playerDTOResult);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/player/{id}")
    ResponseEntity<?> deletePlayer(@PathVariable String id) {
        Map<String, Object> map = new LinkedHashMap<>();
        Mono<Boolean> result = playerService.deletePlayer(id);
        map.put("success", result);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/player/{id}/transactions")
    Mono<WalletSummaryDTO> getPlayerWalletTransactions(@PathVariable String id) {
        return playerService.findPlayerWalletTransactions(id);
    }
}