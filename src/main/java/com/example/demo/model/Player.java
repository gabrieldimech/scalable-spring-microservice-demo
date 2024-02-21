package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder(toBuilder = true)
@Getter
@Setter
@Document("players")
public class Player {
    @Id
    String id;
    String name;
    String surname;
    String username;
    String email;
    Wallet wallet;
}
