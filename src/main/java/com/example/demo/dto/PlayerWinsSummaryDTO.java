package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class PlayerWinsSummaryDTO {
    @Id
    String playerId;
    BigDecimal totalWins;
}
