package org.guccikray.creditcardmanagesystem.exception;

public class CardIsExpiredException extends RuntimeException {

    public CardIsExpiredException() {

    }

    public CardIsExpiredException(String message) {
        super(message);
    }
}
