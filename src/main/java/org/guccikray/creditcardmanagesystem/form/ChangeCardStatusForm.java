package org.guccikray.creditcardmanagesystem.form;

public class ChangeCardStatusForm {

    private String lastFourDigits;

    private String status;

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public ChangeCardStatusForm setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ChangeCardStatusForm setStatus(String status) {
        this.status = status;
        return this;
    }
}
