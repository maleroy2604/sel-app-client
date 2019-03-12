package com.selclientapp.selapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelApiToken {

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    public SelApiToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return getAccessToken();
    }
}
