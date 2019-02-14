package com.selclientapp.selapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExchangeOcurence {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("exchange_id")
    @Expose
    private Integer exchangeId;

    @SerializedName("participant_id")
    @Expose
    private Integer participantId;

    @SerializedName("participant")
    @Expose
    private String participant;

    @SerializedName("hours")
    @Expose
    private Integer hours;


    public ExchangeOcurence(Integer id, int participantId, Integer exchangeId) {
        this.id = id;
        this.participantId = participantId;
        this.exchangeId = exchangeId;
    }

    public Integer getId() {
        return id;
    }

    public String getParticipant() {
        return participant;
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
}
