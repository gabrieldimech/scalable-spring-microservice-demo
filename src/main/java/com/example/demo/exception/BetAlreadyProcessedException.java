package com.example.demo.exception;

public class BetAlreadyProcessedException extends RuntimeException {
    public BetAlreadyProcessedException(String message){
        super(message);
    }
}
