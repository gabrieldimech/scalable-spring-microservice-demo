package com.example.demo.repository;

import com.example.demo.model.Player;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;

public class CustomPlayerRepositoryImpl implements CustomPlayerRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public CustomPlayerRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

   /* @Override
    public Mono<Player> add(Player player) {
        return reactiveMongoTemplate.insert(player);
    }*/

    @Override
    public Mono<Player> update(Player player) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(player.getUsername()));
        Update update = new Update();
        update.set("name", player.getName());
        update.set("surname", player.getSurname());
        update.set("email", player.getEmail());
        update.set("wallet", player.getWallet());
        return reactiveMongoTemplate.findAndModify(query, update, Player.class);
    }

  /*  @Override
    public Optional<Player> findByUsername(String username) {
        return ;
    }*/
}
