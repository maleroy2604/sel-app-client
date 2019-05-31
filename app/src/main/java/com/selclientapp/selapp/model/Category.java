package com.selclientapp.selapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("owner")
    @Expose
    private Integer owner;

    @SerializedName("imagename")
    @Expose
    private String imageName;

    @SerializedName("category")
    @Expose
    private String category;

    public Category( String category, Integer owner){
        this.category = category;
        this.owner = owner;
        this.imageName = category;
    }

    public Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return this.category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwnerId() {
        return owner;
    }

    public void setOwnerId(Integer ownerId) {
        this.owner = owner;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (owner == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(owner);
        }
        dest.writeString(category);
        dest.writeString(imageName);
    }

    protected Category(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            owner = null;
        } else {
            owner = in.readInt();
        }
        category = in.readString();
        imageName = in.readString();
    }

}
