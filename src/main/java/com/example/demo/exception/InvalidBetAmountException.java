package com.example.demo.exception;

public class InvalidBetAmountException extends RuntimeException {
    public InvalidBetAmountException(String message) {
        super(message);
    }
}
