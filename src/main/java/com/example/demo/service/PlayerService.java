package com.example.demo.service;

import com.example.demo.dto.PlayerDTO;
import com.example.demo.dto.WalletSummaryDTO;

import java.util.Optional;

public interface PlayerService {

    void savePlayer(PlayerDTO playerDTO);
    void updatePlayer(String playerId, PlayerDTO playerDTO);
    Optional<PlayerDTO> findPlayer(String playerId);
    void deletePlayer(String id);
    WalletSummaryDTO findPlayerWalletTransactions(String playerId);
}
