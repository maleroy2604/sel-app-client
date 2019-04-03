package com.selclientapp.selapp.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumberLimits {

    @SerializedName("numberlimitmax")
    @Expose
    private int numberLimitMax;

    @SerializedName("numberlimitmin")
    @Expose
    private int numberLimitMin;

    public NumberLimits() {
        this.setNumberLimitMax(15);
        this.setGetNumberLimitMin(0);
    }

    public void setNumberLimitMax(int numberLimitMax) {
        this.numberLimitMax = numberLimitMax;
    }

    public void setGetNumberLimitMin(int numberLimitMin) {
        this.numberLimitMin = numberLimitMin;
    }

    public int getNumberLimitMax() {
        return numberLimitMax;
    }

    public int getNumberLimitMin() {
        return numberLimitMin;
    }

    public void increaseLimit(int limit) {
        setGetNumberLimitMin(numberLimitMax);
        numberLimitMax += limit;
    }

    public void decreaseLimit() {
        setGetNumberLimitMin(0);
        setNumberLimitMax(15);
    }
}
