package com.waitty.waiter.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.waitty.waiter.utility.Utility;
import java.util.ArrayList;

public class Category implements Parcelable {
    int id;
    String name;
    private ArrayList<SubCategory> subCategories = null;

    protected Category(Parcel in) {
        id = in.readInt();
        name = in.readString();
        subCategories = in.createTypedArrayList(SubCategory.CREATOR);
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

    public ArrayList<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(subCategories);
    }
}
