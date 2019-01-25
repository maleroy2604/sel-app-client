package com.selclientapp.selapp.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = CASCADE))
public class Exchange {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("exchangeId")
    @Expose
    private int exchangeId;

    @SerializedName("name")
    @Expose
    private int name;

    @SerializedName("userId")
    @Expose
    private int userId;

    public Exchange(int exchangeId, int name, int userId) {
        this.exchangeId = exchangeId;
        this.name = name;
        this.userId = userId;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
