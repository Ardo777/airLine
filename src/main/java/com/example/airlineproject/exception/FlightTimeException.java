package com.example.airlineproject.exception;

public class FlightTimeException extends RuntimeException{
    public FlightTimeException() {
    }

    public FlightTimeException(String message) {
        super(message);
    }

    public FlightTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlightTimeException(Throwable cause) {
        super(cause);
    }
}
