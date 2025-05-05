package org.guccikray.creditcardmanagesystem.db.entity;

import jakarta.persistence.*;
import org.guccikray.creditcardmanagesystem.db.enums.CardStatus;

import java.time.LocalDate;

@NamedEntityGraph(
    name = "card-with-user",
    attributeNodes = {
        @NamedAttributeNode("user")
    }
)
@Entity
@Table(name = "credit_cards")
public class CreditCard extends BaseEntity {
    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "last_four_digits")
    private Integer lastFourDigits;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

    @Column(name = "balance")
    private Long balance;

    public String getCardNumber() {
        return cardNumber;
    }

    public CreditCard setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public Integer getLastFourDigits() {
        return lastFourDigits;
    }

    public CreditCard setLastFourDigits(Integer lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public CreditCard setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public User getUser() {
        return user;
    }

    public CreditCard setUser(User user) {
        this.user = user;
        return this;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public CreditCard setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public CreditCard setCardStatus(CardStatus cardStatus) {
        this.cardStatus = cardStatus;
        return this;
    }

    public Long getBalance() {
        return balance;
    }

    public CreditCard setBalance(Long balance) {
        this.balance = balance;
        return this;
    }
}
