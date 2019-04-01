package com.selclientapp.selapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelApiToken {

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    @SerializedName("user")
    @Expose
    private User user;


    public SelApiToken(String accessToken, String refreshToken, User user) {
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);
        this.user = new User(user);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken =  refreshToken;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return getAccessToken();
    }
}
