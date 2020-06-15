package com.waitty.waiter.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.waitty.waiter.utility.Utility;


public class Waiter implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("key")
    @Expose
    private String key;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Utility.checkNull(this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return Utility.checkNull(this.key);
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.key);
    }

    public Waiter() {
    }

    protected Waiter(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.key = in.readString();
    }

    public static final Creator<Waiter> CREATOR = new Creator<Waiter>() {
        @Override
        public Waiter createFromParcel(Parcel source) {
            return new Waiter(source);
        }

        @Override
        public Waiter[] newArray(int size) {
            return new Waiter[size];
        }
    };
}