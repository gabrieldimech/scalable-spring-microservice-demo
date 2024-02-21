package com.example.demo.controller;

import com.example.demo.dto.PlayerDTO;
import com.example.demo.dto.WalletSummaryDTO;
import com.example.demo.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class PlayerController {

    final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/player")
    ResponseEntity<?> createPlayer(@RequestBody PlayerDTO playerDTO) {
        Map<String, Object> map = new LinkedHashMap<>();
        playerService.savePlayer(playerDTO);

        map.put("success", true);
        map.put("message", "Player Saved Successfully!");

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("/player/{id}")
    ResponseEntity<?> getPlayer(@PathVariable String id) {
        Map<String, Object> map = new LinkedHashMap<>();

        Optional<PlayerDTO> playerDTOOptional = playerService.findPlayer(id);
        if (playerDTOOptional.isPresent()) {
            map.put("success", true);
            map.put("player", playerDTOOptional.get());
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("success", false);
            map.put("message", "Player Not Found!");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/player/{id}")
    ResponseEntity<?> updatePlayer(@PathVariable String id, @RequestBody PlayerDTO playerDTO) {
        Map<String, Object> map = new LinkedHashMap<>();

        playerService.updatePlayer(id, playerDTO);

        map.put("success", true);
        map.put("message", "Player Saved Successfully!");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/player/{id}")
    ResponseEntity<?> deletePlayer(@PathVariable String id) {
        playerService.deletePlayer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/player/{id}/transactions")
    ResponseEntity<?> getPlayerWalletTransactions(@PathVariable String id) {
        Map<String, Object> map = new LinkedHashMap<>();

        WalletSummaryDTO walletSummaryDTO = playerService.findPlayerWalletTransactions(id);
        map.put("success", true);
        map.put("walletSummary", walletSummaryDTO);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}