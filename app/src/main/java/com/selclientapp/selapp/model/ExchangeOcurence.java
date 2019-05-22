package com.selclientapp.selapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class ExchangeOcurence {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("exchangeId")
    @Expose
    private Integer exchangeId;

    @SerializedName("participantId")
    @Expose
    private Integer participantId;

    @SerializedName("participantName")
    @Expose
    private String participantName;

    @SerializedName("hours")
    @Expose
    private Integer hours;


    public ExchangeOcurence(int participantId, Integer exchangeId, String participantName) {
        this.participantId = participantId;
        this.exchangeId = exchangeId;
        this.participantName = participantName;
    }

    public Integer getId() {
        return id;
    }

    public String getParticipantName() {
        return participantName;
    }

    public Integer getParticipantId() {
        return participantId;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    @NonNull
    @Override
    public String toString() {
        return id + " " + exchangeId + " " + participantId + " " + participantName + "hours " + hours;
    }
}
