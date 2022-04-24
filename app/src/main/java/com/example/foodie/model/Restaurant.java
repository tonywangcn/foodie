package com.example.foodie.model;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.DrawableRes;

import com.example.foodie.DetailActivity;

public class Restaurant {
    private Integer id;
    private String name;
    private Float rating;
    private String image;
    private String category;
    private String description;
    private String address;
    private Integer openTime;
    private Integer closeTime;
    private Boolean hasFreeDelivery;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name, Float rating, String image, String category, String description, String address, Integer openTime, Integer closeTime, Boolean freeDelivery) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.image = image;
        this.category = category;
        this.description = description;
        this.address = address;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.hasFreeDelivery = freeDelivery;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRating() {
        return this.rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getHasFreeDelivery() {
        return hasFreeDelivery;
    }

    public void setHasFreeDelivery(Boolean hasFreeDelivery) {
        this.hasFreeDelivery = hasFreeDelivery;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Integer openTime) {
        this.openTime = openTime;
    }

    public Integer getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Integer closeTime) {
        this.closeTime = closeTime;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", image='" + image + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", hasFreeDelivery=" + hasFreeDelivery +
                '}';
    }

    static Intent starter(Context context, String name, @DrawableRes int imageResId) {
        Intent detailIntent = new Intent(context, DetailActivity.class);
//        detailIntent.putExtra(TITLE_KEY, title);
//        detailIntent.putExtra(IMAGE_KEY, imageResId);
        return detailIntent;
    }

}
