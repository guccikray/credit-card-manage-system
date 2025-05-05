package org.guccikray.creditcardmanagesystem.repository;

import jakarta.validation.constraints.NotNull;
import org.guccikray.creditcardmanagesystem.db.entity.CreditCard;
import org.guccikray.creditcardmanagesystem.db.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    @EntityGraph(value = "card-with-user")
    Page<CreditCard> findAllByUserId(
        @NotNull Long userId,
        @NotNull Pageable pageable
    );

    @EntityGraph(value = "card-with-user")
    Page<CreditCard> findAllByUserIdAndCardStatus(
            @NotNull Long userId,
            @NotNull CardStatus status,
            @NotNull Pageable pageable
    );

    @EntityGraph(value = "card-with-user")
    Optional<CreditCard> findByUserIdAndLastFourDigits(
        @NotNull Long userId,
        @NotNull Integer lastFourDigits
    );

    @Query("from CreditCard cc where cc.userId = :userId " +
        "and cc.lastFourDigits in :cardsLastFourDigits")
    List<CreditCard> findTwoCardsByUserIdAndLastFourDigitsIn(
        @NonNull Long userId,
        @NonNull List<Integer> cardsLastFourDigits
    );

}
