package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.PlayerAlreadyExistsException;
import com.example.demo.exception.PlayerNotFoundException;
import com.example.demo.model.Currency;
import com.example.demo.model.Player;
import com.example.demo.model.Wallet;
import com.example.demo.model.WalletTransaction;
import com.example.demo.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final WalletTransactionService walletTransactionService;
    private final BigDecimal STARTING_BALANCE = BigDecimal.valueOf(1000);
    private final Currency defaultCurrency = Currency.CREDIT;

    public PlayerServiceImpl(PlayerRepository playerRepository, WalletTransactionService walletTransactionService) {
        this.playerRepository = playerRepository;
        this.walletTransactionService = walletTransactionService;
    }

    @Override
    @Transactional
    public void savePlayer(PlayerDTO playerDTO) {
        Optional<Player> existingPlayerOptional = playerRepository.findByUsername(playerDTO.username());
        if (existingPlayerOptional.isPresent()) {
            throw new PlayerAlreadyExistsException(MessageFormat.format("Player with id: {0} already exists", playerDTO.id()));
        }

        final Player player = playerDTOToPlayerMapper(playerDTO).toBuilder()
                .wallet(Wallet.builder()
                        .id(UUID.randomUUID().toString())
                        .balance(STARTING_BALANCE)
                        .currency(defaultCurrency)
                        .playerId(playerDTO.id())
                        .build())
                .build();

        //add new player
        playerRepository.save(player);

        //add deposit transaction
        walletTransactionService.saveDepositTransaction(player.getWallet().getId(), player.getId(), STARTING_BALANCE, UUID.randomUUID().toString());
    }

    @Override
    public void updatePlayer(String playerId, PlayerDTO playerDTO) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        if (optionalPlayer.isPresent()) {
            Player updatedPlayer = playerDTOToPlayerMapper(playerDTO);
            playerRepository.save(updatedPlayer);
        } else {
            throw new PlayerNotFoundException(MessageFormat.format("Player with id: {0} not found", playerId));
        }
    }

    @Override
    public Optional<PlayerDTO> findPlayer(String playerId) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        return optionalPlayer.map(this::playerToDTOMapper);
    }

    public WalletDTO walletToWalletDTO(Wallet wallet) {
        return new WalletDTO(wallet.getId(), wallet.getPlayerId(), wallet.getBalance(), wallet.getCurrency());
    }

    @Override
    public void deletePlayer(String playerId) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        if (optionalPlayer.isPresent()) {
            playerRepository.delete(optionalPlayer.get());
        } else {
            throw new PlayerNotFoundException("Player with id:" + playerId + " not found");
        }
    }

    @Override
    public WalletSummaryDTO findPlayerWalletTransactions(String playerId) {
        Optional<PlayerDTO> playerDTOOptional = findPlayer(playerId);
        if (playerDTOOptional.isEmpty()) {
            throw new PlayerNotFoundException("Player with id:" + playerId + " not found");
        }
        PlayerDTO playerDTO = playerDTOOptional.get();
        List<WalletTransaction> transactionList = walletTransactionService.findByPlayerId(playerId);

        return new WalletSummaryDTO(playerDTO.wallet().getId(), playerId, playerDTO.wallet().getBalance(), playerDTO.wallet().getCurrency(), transactionList);
    }

    private Player playerDTOToPlayerMapper(PlayerDTO playerDTO) {
        return Player.builder()
                .name(playerDTO.name())
                .id(playerDTO.id())
                .surname(playerDTO.surname())
                .username(playerDTO.username())
                .email(playerDTO.email())
                .wallet(playerDTO.wallet())
                .build();
    }

    private PlayerDTO playerToDTOMapper(Player player) {
        return new PlayerDTO(player.getId(), player.getName(), player.getSurname(), player.getUsername(), player.getEmail(), player.getWallet());
    }
}
