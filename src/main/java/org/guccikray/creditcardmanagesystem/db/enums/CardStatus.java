package org.guccikray.creditcardmanagesystem.db.enums;

import java.util.Objects;
import java.util.Set;

public enum CardStatus {
    ACTIVE,
    BLOCKED,
    EXPIRED;

    public static final Set<CardStatus> settableStatuses = Set.of(ACTIVE, BLOCKED);

    public static boolean valid(String name) {
        if (Objects.isNull(name)) {
            return false;
        }

        return settableStatuses.stream()
            .anyMatch(e -> e.name().equalsIgnoreCase(name));
    }
}
