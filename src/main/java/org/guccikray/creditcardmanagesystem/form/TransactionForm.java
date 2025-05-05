package org.guccikray.creditcardmanagesystem.form;

public class TransactionForm {

    private String sourceCardLastFourDigits;

    private String destinationCardLastFourDigits;

    private String amount;

    public String getSourceCardLastFourDigits() {
        return sourceCardLastFourDigits;
    }

    public TransactionForm setSourceCardLastFourDigits(String sourceCardLastFourDigits) {
        this.sourceCardLastFourDigits = sourceCardLastFourDigits;
        return this;
    }

    public String getDestinationCardLastFourDigits() {
        return destinationCardLastFourDigits;
    }

    public TransactionForm setDestinationCardLastFourDigits(String destinationCardLastFourDigits) {
        this.destinationCardLastFourDigits = destinationCardLastFourDigits;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public TransactionForm setAmount(String amount) {
        this.amount = amount;
        return this;
    }
}
