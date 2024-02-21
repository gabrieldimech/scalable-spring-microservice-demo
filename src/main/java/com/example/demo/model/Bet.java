package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@Document("bets")
public class Bet {
    @Id
    private String id;
    private String playerId;
    private String gameId;
    private BigDecimal betAmount;
    private BigDecimal winAmount;
}
