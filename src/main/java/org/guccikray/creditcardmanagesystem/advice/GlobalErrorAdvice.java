package org.guccikray.creditcardmanagesystem.advice;

import io.jsonwebtoken.security.SignatureException;
import org.guccikray.creditcardmanagesystem.dto.ErrorResponse;
import org.guccikray.creditcardmanagesystem.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalErrorAdvice {

    // 404
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
        UserNotFoundException.class,
        CardNotFoundException.class
    })
    public ErrorResponse handleNotFound(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }

    // 409
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({
        CardIsExpiredException.class,
        SourceCardDontHaveEnoughAmountException.class,
        UserWithThisEmailAlreadyExistException.class,
        WrongPasswordException.class,
        SignatureException.class
    })
    public ErrorResponse handleConflict(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }

    // 405
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({
        HttpRequestMethodNotSupportedException.class
    })
    public ErrorResponse handleNotAllowed(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }

    // 422
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({
        InvalidRoleException.class,
        InvalidStatusException.class,
        HttpMessageNotReadableException.class,
        MissingServletRequestParameterException.class
    })
    public ErrorResponse handleUnprocessableEntity(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }

    // 422
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({
        MethodArgumentNotValidException.class
    })
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        FieldError error = ex.getBindingResult().getFieldError();
        if (Objects.nonNull(error)) {
            return new ErrorResponse(error.getCode());
        }
        // не должен заходить сюда
        return new ErrorResponse("Invalid input");
    }
}
