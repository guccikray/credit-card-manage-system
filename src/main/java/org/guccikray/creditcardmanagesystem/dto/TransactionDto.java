package org.guccikray.creditcardmanagesystem.dto;

public class TransactionDto {

    private String sourceCardNumber;

    private Long sourceCardBalance;

    private String destinationCardNumber;

    private Long destinationCardBalance;

    private Long amount;

    public String getSourceCardNumber() {
        return sourceCardNumber;
    }

    public TransactionDto setSourceCardNumber(String sourceCardNumber) {
        this.sourceCardNumber = sourceCardNumber;
        return this;
    }

    public Long getSourceCardBalance() {
        return sourceCardBalance;
    }

    public TransactionDto setSourceCardBalance(Long sourceCardBalance) {
        this.sourceCardBalance = sourceCardBalance;
        return this;
    }

    public String getDestinationCardNumber() {
        return destinationCardNumber;
    }

    public TransactionDto setDestinationCardNumber(String destinationCardNumber) {
        this.destinationCardNumber = destinationCardNumber;
        return this;
    }

    public Long getDestinationCardBalance() {
        return destinationCardBalance;
    }

    public TransactionDto setDestinationCardBalance(Long destinationCardBalance) {
        this.destinationCardBalance = destinationCardBalance;
        return this;
    }

    public Long getAmount() {
        return amount;
    }

    public TransactionDto setAmount(Long amount) {
        this.amount = amount;
        return this;
    }
}
