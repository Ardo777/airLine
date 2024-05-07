package com.example.airlineproject.exception;

public class RatingOutOfBoundException extends RuntimeException {

    public RatingOutOfBoundException() {
        super("Rating out of bound");
    }
}
