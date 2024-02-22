package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@Document("bet_outcomes")
public class BetOutcome {
    @Id
    private String id;
    private String playerId;
    private String gameId;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal betAmount;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal winAmount;
}
