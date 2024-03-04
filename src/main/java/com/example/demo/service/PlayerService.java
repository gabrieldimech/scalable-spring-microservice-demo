package com.example.demo.service;

import com.example.demo.dto.PlayerDTO;
import com.example.demo.dto.WalletSummaryDTO;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface PlayerService {

    Mono<PlayerDTO> savePlayer(PlayerDTO playerDTO);
    Mono<PlayerDTO> updatePlayer(String playerId, PlayerDTO playerDTO);
    Mono<PlayerDTO> findPlayer(String playerId);
    Mono<Boolean> deletePlayer(String id);
    Mono<WalletSummaryDTO> findPlayerWalletTransactions(String playerId);
}
