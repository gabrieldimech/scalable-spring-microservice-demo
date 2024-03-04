package com.example.demo.repository;

import com.example.demo.model.Player;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PlayerRepository extends ReactiveMongoRepository<Player, String>, CustomPlayerRepository {
}
