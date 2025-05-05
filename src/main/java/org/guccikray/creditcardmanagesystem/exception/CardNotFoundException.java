package org.guccikray.creditcardmanagesystem.exception;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException() {

    }

    public CardNotFoundException(String message) {
        super(message);
    }
}
