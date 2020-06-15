package com.waitty.waiter.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.waitty.waiter.utility.Utility;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomizationCategory implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("dishId")
    @Expose
    private int dishId;
    @SerializedName("customization_category_options")
    @Expose
    private ArrayList<CustomizationCategoryOption> customizationCategoryOptions = null;

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

    public String getType() {
        return Utility.checkNull(this.type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public ArrayList<CustomizationCategoryOption> getCustomizationCategoryOptions() {
        return customizationCategoryOptions;
    }

    public void setCustomizationCategoryOptions(ArrayList<CustomizationCategoryOption> customizationCategoryOptions) {
        this.customizationCategoryOptions = customizationCategoryOptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeInt(this.dishId);
        dest.writeList(this.customizationCategoryOptions);
    }

    public CustomizationCategory() {
    }

    protected CustomizationCategory(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.type = in.readString();
        this.dishId = in.readInt();
        this.customizationCategoryOptions = new ArrayList<CustomizationCategoryOption>();
        in.readList(this.customizationCategoryOptions, CustomizationCategoryOption.class.getClassLoader());
    }

    public static final Parcelable.Creator<CustomizationCategory> CREATOR = new Parcelable.Creator<CustomizationCategory>() {
        @Override
        public CustomizationCategory createFromParcel(Parcel source) {
            return new CustomizationCategory(source);
        }

        @Override
        public CustomizationCategory[] newArray(int size) {
            return new CustomizationCategory[size];
        }
    };
}
