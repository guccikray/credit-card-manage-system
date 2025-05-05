package org.guccikray.creditcardmanagesystem.repository;

import jakarta.validation.constraints.NotNull;
import org.guccikray.creditcardmanagesystem.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@NotNull String email);

    Optional<User> findByEmail(@NotNull String email);

}
