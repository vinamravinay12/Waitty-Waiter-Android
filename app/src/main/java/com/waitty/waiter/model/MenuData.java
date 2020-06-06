package com.waitty.waiter.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class MenuData implements Parcelable {
    private ArrayList<Category> category = null;

    protected MenuData(Parcel in) {
        category = in.createTypedArrayList(Category.CREATOR);
    }

    public static final Creator<MenuData> CREATOR = new Creator<MenuData>() {
        @Override
        public MenuData createFromParcel(Parcel in) {
            return new MenuData(in);
        }

        @Override
        public MenuData[] newArray(int size) {
            return new MenuData[size];
        }
    };

    public ArrayList<Category> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<Category> category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(category);
    }
}
