package com.example.demo.service;

import com.example.demo.dto.LeaderBoardDTO;
import com.example.demo.dto.PlayerWinsSummaryDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class LeaderBoardServiceImpl implements LeaderBoardService {
    private final MongoTemplate mongoTemplate;

    public LeaderBoardServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    //@Async
    @Override
    public LeaderBoardDTO getLeaderBoard() {
        GroupOperation groupByPlayerIdAndSumWinAmount = group("playerId")
                .sum("winAmount")
                .as("totalWins");

        SortOperation sortByTotalWins = sort(Sort.by(Sort.Direction.DESC, "totalWins"));

        Aggregation aggregation = newAggregation(
                groupByPlayerIdAndSumWinAmount, sortByTotalWins);

        AggregationResults<PlayerWinsSummaryDTO> result = mongoTemplate.aggregate(
                aggregation, "bet_outcomes", PlayerWinsSummaryDTO.class);

        return new LeaderBoardDTO(result.getMappedResults());
    }
}
