package com.example.foodie.model;

import android.app.Application;

import java.util.ArrayList;

public class Global extends Application {
    private ArrayList<Restaurant> restaurants;

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
