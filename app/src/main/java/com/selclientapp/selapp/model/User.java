package com.selclientapp.selapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("confirmpassword")
    @Expose
    private String confirmpassword;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("counterHours")
    @Expose
    private Integer counterhours;

    @SerializedName("avatarurl")
    @Expose
    private String avatarurl;


    public User(String username, String password, String confirmpassword, String email, int counterhours) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setConfirmpassword(confirmpassword);
        setCounterhours(counterhours);
    }

    public User(User user) {
        user.setId(user.getId());
        user.setUsername(user.getUsername());
        user.setPassword(user.getPassword());
        user.setEmail(user.getEmail());
        user.setCounterhours(user.getCounterhours());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCounterhours() {
        return counterhours;
    }

    public void setCounterhours(Integer counterhours) {
        this.counterhours = counterhours;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

