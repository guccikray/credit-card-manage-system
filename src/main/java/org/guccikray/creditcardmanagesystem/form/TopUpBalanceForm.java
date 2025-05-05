package org.guccikray.creditcardmanagesystem.form;

public class TopUpBalanceForm {

    private String cardLastFourDigits;

    private String amount;

    public String getCardLastFourDigits() {
        return cardLastFourDigits;
    }

    public TopUpBalanceForm setCardLastFourDigits(String cardLastFourDigits) {
        this.cardLastFourDigits = cardLastFourDigits;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public TopUpBalanceForm setAmount(String amount) {
        this.amount = amount;
        return this;
    }
}
