package com.example.demo.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import java.util.Collection;
import java.util.Collections;

@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb://root:pass@localhost:27017/test?authSource=admin");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), "test");
    }

    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton("com.example.demo.model");
    }

}