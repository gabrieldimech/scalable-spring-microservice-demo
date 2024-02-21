package com.example.demo.service;

import com.example.demo.exception.PlayerNotFoundException;
import com.example.demo.model.Player;
import com.example.demo.model.TransactionType;
import com.example.demo.model.WalletTransaction;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.WalletTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final PlayerRepository playerRepository;
    Logger logger = LoggerFactory.getLogger(WalletTransactionServiceImpl.class);

    public WalletTransactionServiceImpl(WalletTransactionRepository walletTransactionRepository, PlayerRepository playerRepository) {
        this.walletTransactionRepository = walletTransactionRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional //todo test
    public void saveBetAndUpdateBalanceTransactions(String betReferenceId, BigDecimal betAmount, BigDecimal winAmount, String playerId) {
        Optional<Player> playerOptional = playerRepository.findById(playerId);

        if (playerOptional.isEmpty()) {
            throw new PlayerNotFoundException(MessageFormat.format("Player with id: {0} not found", playerId));
        }

        Player player = playerOptional.get();

        BigDecimal oldBalance = player.getWallet().getBalance();

        List<WalletTransaction> transactions = new ArrayList<>();

        transactions.add(buildTransaction(betAmount, player.getWallet().getId(), playerId, TransactionType.BET, betReferenceId));
        logger.debug(MessageFormat.format("BET Transactions created for betReferenceId: {0}", betReferenceId));

        BigDecimal newBalance = oldBalance.subtract(betAmount);

        if (winAmount.compareTo(BigDecimal.ZERO) > 0) {
            transactions.addLast(buildTransaction(winAmount, player.getWallet().getId(), playerId, TransactionType.WIN, null));
            newBalance = newBalance.add(winAmount);
            logger.debug(MessageFormat.format("WIN Transactions created for betReferenceId: {0}", betReferenceId));
        }

        walletTransactionRepository.saveAll(transactions);

        playerRepository.save(player.toBuilder().wallet(player.getWallet().toBuilder().balance(newBalance).build()).build());
        logger.debug(MessageFormat.format("Wallet balance updated for betReferenceId: {0}", betReferenceId));
    }

    @Override
    public void saveDepositTransaction(String walletId, String playerId, BigDecimal amount, String referenceId) {
        walletTransactionRepository.save(buildTransaction(amount, walletId, playerId, TransactionType.DEPOSIT, referenceId));
    }

    @Override
    public List<WalletTransaction> findByPlayerId(String playerId) {
        return walletTransactionRepository.findByPlayerId(playerId);
    }

    protected WalletTransaction buildTransaction(BigDecimal betAmount, String walletId, String playerId, TransactionType transactionType, String referenceId) {
        return WalletTransaction.builder()
                .id(UUID.randomUUID().toString())
                .referenceId(referenceId)
                .amount(betAmount)
                .createdDate(Instant.now())
                .transactionType(transactionType)
                .walletId(walletId)
                .playerId(playerId)
                .build();
    }
}
