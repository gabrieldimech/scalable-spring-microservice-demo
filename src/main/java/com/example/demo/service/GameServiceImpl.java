package com.example.demo.service;

import com.example.demo.exception.*;
import com.example.demo.game.BetRequestDTO;
import com.example.demo.game.Game;
import com.example.demo.game.GameCatalogue;
import com.example.demo.game.GameResult;
import com.example.demo.model.BetOutcome;
import com.example.demo.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;

@Service
public class GameServiceImpl implements GameService {
    private final LeaderBoardServiceImpl leaderBoardService;
    private final PlayerRepository playerRepository;
    private final WalletTransactionService walletTransactionService;
    private final BetOutcomeService betOutcomeService;
    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    public GameServiceImpl(LeaderBoardServiceImpl leaderBoardService, PlayerRepository playerRepository, WalletTransactionService walletTransactionService, BetOutcomeService betOutcomeService) {
        this.leaderBoardService = leaderBoardService;
        this.playerRepository = playerRepository;
        this.walletTransactionService = walletTransactionService;
        this.betOutcomeService = betOutcomeService;
    }

    @Override
    public GameResult playGame(BetRequestDTO betRequestDTO, String gameName) {
        validateBetRequest(betRequestDTO);

        final Game game;

        try {
            game = GameCatalogue.valueOf(gameName).getGame();
        } catch (Exception e) {
            throw new GameNotSupportedException(MessageFormat.format("Game with name: ${0} is not currently supported", gameName));
        }

        game.validateBetRequest(betRequestDTO);

        logger.debug(MessageFormat.format("Bet request with reference id: {0} was validated successfully, playing Numbers Game", betRequestDTO.betReferenceId()));

        GameResult gameResult = game.playGameRound(betRequestDTO.betAmount(), betRequestDTO.betNumber());

        if (gameResult.getWinAmount().compareTo(BigDecimal.ZERO) > 0) {
            leaderBoardService.clearLeaderboardCache();
        }

        walletTransactionService.saveBetAndUpdateBalanceTransactions(betRequestDTO.betReferenceId(), betRequestDTO.betAmount(), gameResult.getWinAmount(), betRequestDTO.playerId());

        //save bet outcome
        betOutcomeService.saveBetOutcome(BetOutcome.builder()
                        .gameId(gameName)
                        .playerId(betRequestDTO.playerId())
                        .id(betRequestDTO.betReferenceId())
                        .betAmount(betRequestDTO.betAmount())
                        .winAmount(gameResult.getWinAmount())
                        .build())
                .subscribe(betOutcome -> logger.info(MessageFormat.format("Bet with reference {0} saved", betRequestDTO.betReferenceId())));

        return gameResult;
    }

    void validateBetRequest(BetRequestDTO betDTORequest) {
        //idempotency check
        betOutcomeService.findById(betDTORequest.betReferenceId())
                .handle((betOutcome, sink) -> {
                    if (betOutcome != null) {
                        sink.error(new BetAlreadyProcessedException(MessageFormat.format("Bet with reference id: {0} has already been processed", betDTORequest.betReferenceId())));
                    }
                });

        //validate player id
        playerRepository.findById(betDTORequest.playerId())
                .handle((player, sink) -> {
                    if (player == null) {
                        sink.error(new PlayerNotFoundException(MessageFormat.format("Player with id: {0} not found", betDTORequest.playerId())));
                    } else {
                        //validate bet amount
                        BigDecimal walletBalance = player.getWallet().getBalance();
                        if (betDTORequest.betAmount().compareTo(BigDecimal.ZERO) < 0) {
                            sink.error(new InvalidBetAmountException(MessageFormat.format("Invalid bet amount: {0} cannot be smaller than 0", betDTORequest.betAmount())));
                        }
                        if (walletBalance.compareTo(betDTORequest.betAmount()) < 0) {
                            sink.error(new InsufficientFundsException(MessageFormat.format("Insufficient funds in wallet: {0} for bet amount: {1}", walletBalance, betDTORequest.betAmount())));
                        }
                    }
                });
    }
}
