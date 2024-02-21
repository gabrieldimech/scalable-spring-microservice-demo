package com.example.demo.exception;

public class InvalidBetNumberException extends RuntimeException {
    public InvalidBetNumberException(String message) {
        super(message);
    }
}
