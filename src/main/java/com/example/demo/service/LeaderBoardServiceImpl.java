package com.example.demo.service;

import com.example.demo.dto.LeaderBoardDTO;
import com.example.demo.dto.PlayerWinsSummaryDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class LeaderBoardServiceImpl implements LeaderBoardService {
    private final MongoTemplate mongoTemplate;

    public LeaderBoardServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Cacheable("leaderboard")
    @Override
    public Mono<LeaderBoardDTO> getLeaderBoard() {
        GroupOperation groupByPlayerIdAndSumWinAmount = group("playerId")
                .sum("winAmount")
                .as("totalWins");

        MatchOperation filterPlayers = match(new Criteria("totalWins").gt(0));

        SortOperation sortByTotalWins = sort(Sort.by(Sort.Direction.DESC, "totalWins"));

        Aggregation aggregation = newAggregation(
                groupByPlayerIdAndSumWinAmount, filterPlayers, sortByTotalWins);

        AggregationResults<PlayerWinsSummaryDTO> result = mongoTemplate.aggregate(
                aggregation, "bet_outcomes", PlayerWinsSummaryDTO.class);

        return Mono.just(new LeaderBoardDTO(result.getMappedResults()));
    }

    @CacheEvict(value = "leaderboard", allEntries = true)
    public void clearLeaderboardCache() {
    }
}
