package com.example.demo.dto;

import com.example.demo.model.Wallet;

public record PlayerDTO(String id, String name, String surname, String username, String email, Wallet wallet) {
}
