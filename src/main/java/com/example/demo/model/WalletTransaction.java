package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Getter
@Setter
@Document("wallet_transactions")
public class WalletTransaction {
    @Id
    String id;
    String referenceId; //in case of bet , the bet id, in case of transfer, the transfer id
    String walletId;
    String playerId;
    @Field(targetType = FieldType.DECIMAL128)
    BigDecimal amount;
    TransactionType transactionType;
    Instant createdDate;
}
