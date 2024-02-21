package com.example.demo.repository;

import com.example.demo.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

//@EnableMongoRepositories(basePackages = "com.baeldung.repository")
public interface PlayerRepository extends MongoRepository<Player, String> {//, QuerydslPredicateExecutor<Player>
    //List<Player> getPlayersOrderedByWins();
    Optional<Player> findByUsername(String username);
}
