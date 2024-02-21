package com.example.demo.service;

import com.example.demo.exception.BetAlreadyProcessedException;
import com.example.demo.exception.GameNotSupportedException;
import com.example.demo.exception.InsufficientFundsException;
import com.example.demo.exception.PlayerNotFoundException;
import com.example.demo.game.*;
import com.example.demo.repository.BetRepository;
import com.example.demo.repository.PlayerRepository;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;

@Service
public class GameServiceImpl implements GameService {
    private final PlayerRepository playerRepository;
    private final WalletTransactionService walletTransactionService;
    private final BetRepository betRepository;
    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    public GameServiceImpl(PlayerRepository playerRepository, WalletTransactionService walletTransactionService, BetRepository betRepository) {
        this.playerRepository = playerRepository;
        this.walletTransactionService = walletTransactionService;
        this.betRepository = betRepository;
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

        walletTransactionService.saveBetAndUpdateBalanceTransactions(betRequestDTO.betReferenceId(), betRequestDTO.betAmount(), gameResult.getWinAmount(), betRequestDTO.playerId());

        return gameResult;
    }

    void validateBetRequest(BetRequestDTO betDTORequest) {
        //idempotency check
        val existingBet = betRepository.findById(betDTORequest.betReferenceId());
        if (existingBet.isPresent()) {
            throw new BetAlreadyProcessedException(MessageFormat.format("Bet with reference id: {0} has already been processed", betDTORequest.betReferenceId()));
        }

        //validate player id
        val player = playerRepository.findById(betDTORequest.playerId());
        if (player.isEmpty()) {
            throw new PlayerNotFoundException(MessageFormat.format("Player with id: {0} not found", betDTORequest.playerId()));
        }

        //validate bet amount
        val walletBalance = player.get().getWallet().getBalance();
        if (betDTORequest.betAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBetAmountException(MessageFormat.format("Invalid bet amount: {0} cannot be smaller than 0", betDTORequest.betAmount()));
        }

        if (walletBalance.compareTo(betDTORequest.betAmount()) < 0) {
            throw new InsufficientFundsException(MessageFormat.format("Insufficient funds in wallet: {0} for bet amount: {1}", walletBalance, betDTORequest.betAmount()));
        }
    }
}
