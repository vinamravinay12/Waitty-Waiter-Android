package com.waitty.waiter.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.waitty.waiter.utility.Utility;

import java.io.Serializable;
import java.util.ArrayList;

public class DishData implements Parcelable {

    /*double selling_price,price;
    String name,dish_image,description;
    boolean is_soldout;

    public double getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return Utility.checkNull(this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDish_image() {
        return Utility.checkNull(this.dish_image);
    }

    public void setDish_image(String dish_image) {
        this.dish_image = dish_image;
    }

    public String getDescription() {
        return Utility.checkNull(this.description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_soldout() {
        return is_soldout;
    }

    public void setIs_soldout(boolean is_soldout) {
        this.is_soldout = is_soldout;
    }

    @BindingAdapter({"dishImage"})
    public static void loadImage(ImageView view, String imageUrl) {
        if(!imageUrl.isEmpty())
            Picasso.get().load(imageUrl).placeholder(R.drawable.dish_holder).error(R.drawable.dish_holder).into(view);
    }*/

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dish_image")
    @Expose
    private String dishImage="";
    @SerializedName("categoryId")
    @Expose
    private int categoryId;
    @SerializedName("subcategoryId")
    @Expose
    private int subcategoryId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("selling_price")
    @Expose
    private double sellingPrice;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("is_soldout")
    @Expose
    private Boolean isSoldout;
    @SerializedName("customization_categories")
    @Expose
    private ArrayList<CustomizationCategory> customizationCategories = null;

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

    public String getDishImage() {
        return Utility.checkNull(this.dishImage);
    }

    public void setDishImage(String dishImage) {
        this.dishImage = dishImage;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getDescription() {
        return Utility.checkNull(this.description);
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ArrayList<CustomizationCategory> getCustomizationCategories() {
        return customizationCategories;
    }

    public void setCustomizationCategories(ArrayList<CustomizationCategory> customizationCategories) {
        this.customizationCategories = customizationCategories;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.dishImage);
        dest.writeInt(this.categoryId);
        dest.writeInt(this.subcategoryId);
        dest.writeString(this.description);
        dest.writeDouble(this.sellingPrice);
        dest.writeDouble(this.price);
        dest.writeValue(this.isSoldout);
        dest.writeList(this.customizationCategories);
    }

    public DishData() {
    }

    protected DishData(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.dishImage = in.readString();
        this.categoryId = in.readInt();
        this.subcategoryId = in.readInt();
        this.description = in.readString();
        this.sellingPrice = in.readDouble();
        this.price = in.readDouble();
        this.isSoldout = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.customizationCategories = new ArrayList<CustomizationCategory>();
        in.readList(this.customizationCategories, CustomizationCategory.class.getClassLoader());
    }

    public static final Parcelable.Creator<DishData> CREATOR = new Parcelable.Creator<DishData>() {
        @Override
        public DishData createFromParcel(Parcel source) {
            return new DishData(source);
        }

        @Override
        public DishData[] newArray(int size) {
            return new DishData[size];
        }
    };
}
