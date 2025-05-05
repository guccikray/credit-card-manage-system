package org.guccikray.creditcardmanagesystem.form;

public class RegisterUserForm {

    private String name;

    private String surname;

    private String email;

    private String password;

    public String getName() {
        return name;
    }

    public RegisterUserForm setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public RegisterUserForm setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RegisterUserForm setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterUserForm setPassword(String password) {
        this.password = password;
        return this;
    }
}
