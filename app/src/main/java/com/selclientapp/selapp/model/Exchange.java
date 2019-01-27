package com.selclientapp.selapp.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Exchange {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("capacity")
    @Expose
    private Integer capacity;

    @SerializedName("current_capacity")
    @Expose
    private Integer currentCapacity;

    @SerializedName("owner")
    @Expose
    private Integer owner;

    @SerializedName("exchangeocurence")
    @Expose
    private List<Object> exchangeocurence = null;

    @SerializedName("messages")
    @Expose
    private List<Object> messages = null;

    public Exchange(Integer id, String name, String description, String date, Integer capacity, Integer currentCapacity, Integer owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.capacity = capacity;
        this.currentCapacity = currentCapacity;
        this.owner = owner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(Integer currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public List<Object> getExchangeocurence() {
        return exchangeocurence;
    }

    public void setExchangeocurence(List<Object> exchangeocurence) {
        this.exchangeocurence = exchangeocurence;
    }

    public List<Object> getMessages() {
        return messages;
    }

    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }

}