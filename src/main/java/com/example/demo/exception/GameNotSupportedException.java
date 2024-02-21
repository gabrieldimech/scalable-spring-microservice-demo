package com.example.demo.exception;

public class GameNotSupportedException extends RuntimeException {
    public GameNotSupportedException(String message) {
        super(message);
    }
}
