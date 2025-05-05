package org.guccikray.creditcardmanagesystem.model;

import org.guccikray.creditcardmanagesystem.db.enums.CardStatus;

import java.time.LocalDate;

public class CreditCardModel {

    private String cardNumber;

    private String name;

    private String surname;

    private Long userId;

    private LocalDate expirationDate;

    private CardStatus status;

    private Long balance;

    public String getCardNumber() {
        return cardNumber;
    }

    public CreditCardModel setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreditCardModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public CreditCardModel setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public CreditCardModel setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public CreditCardModel setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public CardStatus getStatus() {
        return status;
    }

    public CreditCardModel setStatus(CardStatus status) {
        this.status = status;
        return this;
    }

    public Long getBalance() {
        return balance;
    }

    public CreditCardModel setBalance(Long balance) {
        this.balance = balance;
        return this;
    }
}
