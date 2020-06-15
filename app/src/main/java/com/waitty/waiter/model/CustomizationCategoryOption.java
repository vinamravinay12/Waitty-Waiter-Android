package com.waitty.waiter.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.waitty.waiter.utility.Utility;

import java.io.Serializable;

public class CustomizationCategoryOption implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("customizationCategoryId")
    @Expose
    private int customizationCategoryId;
    @SerializedName("dishId")
    @Expose
    private int dishId;
    @SerializedName("is_default")
    @Expose
    private Boolean isDefault;
    @SerializedName("selling_price")
    @Expose
    private double sellingPrice;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("is_soldout")
    @Expose
    private Boolean isSoldout;

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

    public int getCustomizationCategoryId() {
        return customizationCategoryId;
    }

    public void setCustomizationCategoryId(int customizationCategoryId) {
        this.customizationCategoryId = customizationCategoryId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Boolean getIsSoldout() {
        return isSoldout;
    }

    public void setIsSoldout(Boolean isSoldout) {
        this.isSoldout = isSoldout;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.customizationCategoryId);
        dest.writeInt(this.dishId);
        dest.writeValue(this.isDefault);
        dest.writeDouble(this.sellingPrice);
        dest.writeDouble(this.price);
        dest.writeValue(this.isSoldout);
    }

    public CustomizationCategoryOption() {
    }

    protected CustomizationCategoryOption(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.customizationCategoryId = in.readInt();
        this.dishId = in.readInt();
        this.isDefault = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.sellingPrice = in.readDouble();
        this.price = in.readDouble();
        this.isSoldout = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<CustomizationCategoryOption> CREATOR = new Parcelable.Creator<CustomizationCategoryOption>() {
        @Override
        public CustomizationCategoryOption createFromParcel(Parcel source) {
            return new CustomizationCategoryOption(source);
        }

        @Override
        public CustomizationCategoryOption[] newArray(int size) {
            return new CustomizationCategoryOption[size];
        }
    };
}
