package org.guccikray.creditcardmanagesystem.exception;

public class UserWithThisEmailAlreadyExistException extends RuntimeException {

    public UserWithThisEmailAlreadyExistException() {

    }

    public UserWithThisEmailAlreadyExistException(String message) {
        super(message);
    }
}
