package org.guccikray.creditcardmanagesystem.exception;

public class InvalidStatusException extends RuntimeException {

    public InvalidStatusException() {

    }

    public InvalidStatusException(String message) {
        super(message);
    }
}
