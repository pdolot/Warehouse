package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

public class SignIn {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String userPassword;

    public SignIn(String username, String userPassword) {
        this.username = username;
        this.userPassword = userPassword;
    }

    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
