package org.guccikray.creditcardmanagesystem.exception;

public class SourceCardDontHaveEnoughAmountException extends RuntimeException {

    public SourceCardDontHaveEnoughAmountException() {

    }

    public SourceCardDontHaveEnoughAmountException(String message) {
        super(message);
    }
}
