package com.selclientapp.selapp.api;

public class TokenBody {
    private String username;
    private String password;

    public TokenBody(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return this.username + " " + this.password;
    }
}
