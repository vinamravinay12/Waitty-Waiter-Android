package com.waitty.waiter.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;
import com.waitty.waiter.R;
import com.waitty.waiter.utility.Utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class DishData implements Serializable {

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

    @BindingAdapter({"dishImage"})
    public static void loadImage(ImageView view, String imageUrl) {
        if(!imageUrl.isEmpty())
            Picasso.get().load(imageUrl).placeholder(R.drawable.dish_holder).error(R.drawable.dish_holder).into(view);
    }

}
