package org.guccikray.creditcardmanagesystem.assembler;

import jakarta.validation.constraints.NotNull;
import org.guccikray.creditcardmanagesystem.db.entity.User;
import org.guccikray.creditcardmanagesystem.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler extends AbstractModelAssembler<UserModel, User>{

    @Override
    public UserModel toModel(@NotNull User user) {
        return new UserModel()
            .setUserId(user.getId())
            .setName(user.getName())
            .setSurname(user.getSurname())
            .setEmail(user.getEmail())
            .setRole(user.getRole());
    }
}
