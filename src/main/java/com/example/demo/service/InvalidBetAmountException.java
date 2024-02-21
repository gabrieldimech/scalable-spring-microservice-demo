package com.example.demo.service;

public class InvalidBetAmountException extends RuntimeException {
    InvalidBetAmountException(String message) {
        super(message);
    }
}
