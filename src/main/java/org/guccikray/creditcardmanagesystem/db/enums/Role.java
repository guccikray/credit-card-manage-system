package org.guccikray.creditcardmanagesystem.db.enums;

import java.util.Arrays;
import java.util.Objects;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public static boolean valid(String name) {
        if (Objects.isNull(name)) {
            return false;
        }

        return Arrays.stream(Role.values())
            .anyMatch(e -> e.name().equalsIgnoreCase(name));
    }
}
