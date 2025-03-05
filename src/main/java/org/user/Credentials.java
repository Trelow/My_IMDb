package org.user;

import lombok.Getter;

@Getter
public class Credentials {
    private String email;
    private String password;

    public Credentials() {
        this.email = "";
        this.password = "";
    }

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}