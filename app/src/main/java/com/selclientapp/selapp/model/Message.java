package com.selclientapp.selapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("avatarUrl")
    @Expose
    private String avatarUrl;

    @SerializedName("exchangeId")
    @Expose
    private Integer exchangeId;

    @SerializedName("senderId")
    @Expose
    private Integer senderId;


    public Message(String message, Integer exchangeId, Integer senderId,String  avatarUrl) {
        this.message = message;
        this.exchangeId = exchangeId;
        this.senderId = senderId;
        this.avatarUrl = avatarUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", exchangeId=" + exchangeId +
                ", senderId=" + senderId +
                '}';
    }
}
