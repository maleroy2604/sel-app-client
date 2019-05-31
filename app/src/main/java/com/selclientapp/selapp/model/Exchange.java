package com.selclientapp.selapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Exchange implements Parcelable {

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

    @SerializedName("currentCapacity")
    @Expose
    private Integer currentCapacity;

    @SerializedName("owner")
    @Expose
    private Integer owner;

    @SerializedName("ownerName")
    @Expose
    private String ownerName;

    @SerializedName("avatarUrl")
    @Expose
    private String avatarUrl;

    @SerializedName("exchangeOcurences")
    @Expose
    private List<ExchangeOcurence> exchangeocurences = new ArrayList<>();

    @SerializedName("messages")
    @Expose
    private List<Message> messages = new ArrayList<>();

    @SerializedName("category")
    @Expose
    private String category;

    public Exchange(String name, String description, String ownerName, String date, Integer capacity, Integer owner, String avatarUrl, String category) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.capacity = capacity;
        this.owner = owner;
        this.ownerName = ownerName;
        this.avatarUrl = avatarUrl;
        this.category = category;
    }

    public Exchange (Exchange exchange) {
        this.setId(exchange.getId());
        this.setName(exchange.getName());
        this.setDescription(exchange.getDescription());
        this.setDate(exchange.getDate());
        this.setCapacity(exchange.getCapacity());
        this.setOwner(exchange.getOwner());
        this.setOwnerName(exchange.getOwnerName());
        this.setAvatarUrl(exchange.getAvatarUrl());
        this.setCategory(exchange.getCategory());
    }

    protected Exchange(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        description = in.readString();
        date = in.readString();
        if (in.readByte() == 0) {
            capacity = null;
        } else {
            capacity = in.readInt();
        }
        if (in.readByte() == 0) {
            currentCapacity = null;
        } else {
            currentCapacity = in.readInt();
        }
        if (in.readByte() == 0) {
            owner = null;
        } else {
            owner = in.readInt();
        }
        ownerName = in.readString();
    }

    public static final Creator<Exchange> CREATOR = new Creator<Exchange>() {
        @Override
        public Exchange createFromParcel(Parcel in) {
            return new Exchange(in);
        }

        @Override
        public Exchange[] newArray(int size) {
            return new Exchange[size];
        }
    };

    public void setCurrentCapacity(Integer currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getOwnerName() {
        return ownerName;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCurrentCapacity() {
        if (exchangeocurences == null || exchangeocurences.isEmpty()) {
            return 0;
        } else {
            return this.exchangeocurences.size();
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public Integer getOwner() {
        return owner;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public List<ExchangeOcurence> getExchangeocurence() {
        return exchangeocurences;
    }

    public void removeExchangeOcurecne(ExchangeOcurence exchangeOcurence) {
        exchangeocurences.remove(exchangeOcurence);
    }

    public void addExchangeOcurence(ExchangeOcurence exchangeOcurence) {
        exchangeocurences.add(exchangeOcurence);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(date);
        if (capacity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(capacity);
        }
        if (currentCapacity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(currentCapacity);
        }
        if (owner == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(owner);
        }
        dest.writeString(ownerName);
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", capacity=" + capacity +
                ", currentCapacity=" + currentCapacity +
                ", owner=" + owner +
                ", ownerName='" + ownerName + '\'' +
                ", exchangeocurences=" + exchangeocurences +
                ", messages=" + messages +
                '}';
    }
}