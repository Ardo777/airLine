package com.example.airlineproject.exception;

public class PlaneNotFoundException extends RuntimeException{
    public PlaneNotFoundException() {
    }

    public PlaneNotFoundException(String message) {
        super(message);
    }

    public PlaneNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaneNotFoundException(Throwable cause) {
        super(cause);
    }
}
