package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Builder(toBuilder = true)
@Getter
@Setter
public class Wallet {
    String id;
    String playerId;
    BigDecimal balance;
    Currency currency;
}
