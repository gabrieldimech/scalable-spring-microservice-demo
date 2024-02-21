package com.example.demo.exception;

public record ErrorResponse(int httpStatus, String message) {
}
