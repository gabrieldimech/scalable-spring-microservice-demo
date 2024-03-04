package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Currency;
import com.example.demo.model.Player;
import com.example.demo.model.Wallet;
import com.example.demo.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final WalletTransactionService walletTransactionService;
    private final BigDecimal STARTING_BALANCE = BigDecimal.valueOf(1000);
    private final Currency defaultCurrency = Currency.CREDIT;
    private final Logger logger = LoggerFactory.getLogger(WalletTransactionServiceImpl.class);

    public PlayerServiceImpl(PlayerRepository playerRepository, WalletTransactionService walletTransactionService) {
        this.playerRepository = playerRepository;
        this.walletTransactionService = walletTransactionService;
    }

    @Override
    @Transactional
    //todo test transactions and investigate why calls are > 1200 ms
    //since we have multiple db calls here we will need to investigate adding version field inorder to handle concurrency issues
    public Mono<PlayerDTO> savePlayer(PlayerDTO playerDTO) {
        final Player player = playerDTOToPlayerMapper(playerDTO).toBuilder()
                .wallet(Wallet.builder()
                        .id(UUID.randomUUID().toString())
                        .balance(STARTING_BALANCE)
                        .currency(defaultCurrency)
                        .playerId(playerDTO.id())
                        .build())
                .build();

        //add new player
        return playerRepository.insert(player)
                .flatMap(p -> walletTransactionService.saveDepositTransaction(player.getWallet().getId(), player.getId(), STARTING_BALANCE, UUID.randomUUID().toString())
                        .thenReturn(playerToDTOMapper(p)));
    }

    @Override
    public Mono<PlayerDTO> updatePlayer(String playerId, PlayerDTO playerDTO) {
        Player updatedPlayer = playerDTOToPlayerMapper(playerDTO);
        return playerRepository.update(updatedPlayer)
                .map(this::playerToDTOMapper);
    }

    @Override
    public Mono<PlayerDTO> findPlayer(String playerId) {
        Mono<Player> optionalPlayer = playerRepository.findById(playerId);
        return optionalPlayer.map(this::playerToDTOMapper);
    }

    @Override
    public Mono<Boolean> deletePlayer(String playerId) {
        return playerRepository.findById(playerId).flatMap(playerRepository::delete).then(Mono.fromCallable(() -> Boolean.TRUE)).onErrorComplete(throwable -> Boolean.FALSE);
    }

    @Override
    public Mono<WalletSummaryDTO> findPlayerWalletTransactions(String playerId) {
        return findPlayer(playerId).flatMap(p -> Mono.from(walletTransactionService.findByPlayerId(playerId)
                .collectList()
                .flatMap(walletTransactions -> Mono.just(new WalletSummaryDTO(p.wallet().getId(), playerId, p.wallet().getBalance(), p.wallet().getCurrency(), walletTransactions)))));
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
