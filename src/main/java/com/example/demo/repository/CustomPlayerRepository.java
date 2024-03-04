package com.example.demo.repository;

import com.example.demo.model.Player;
import reactor.core.publisher.Mono;

public interface CustomPlayerRepository {
    //Optional<Player> findByUsername(String username);
//    Mono<Player> add(Player player);
    Mono<Player> update(Player player);
}
