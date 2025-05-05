package org.guccikray.creditcardmanagesystem.model;

import org.guccikray.creditcardmanagesystem.db.enums.Role;

public class UserModel {

    private Long userId;

    private String name;

    private String surname;

    private String email;

    private Role role;

    public Long getUserId() {
        return userId;
    }

    public UserModel setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public UserModel setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public UserModel setRole(Role role) {
        this.role = role;
        return this;
    }
}
