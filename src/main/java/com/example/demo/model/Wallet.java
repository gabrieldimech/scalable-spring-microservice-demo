package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@Setter
public class Wallet {
    String id;
    String playerId;
    @Field(targetType = FieldType.DECIMAL128)
    BigDecimal balance;
    Currency currency;
}
