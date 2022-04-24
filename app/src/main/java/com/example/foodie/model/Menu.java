package com.example.foodie.model;

public class Menu {
    private Integer id;
    private String name;
    private Float price;
    private String image;
    private Integer restaurantId;

    public Menu() {
    }

    public Menu(Integer id, String name, Float price, String image, Integer restaurantId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.restaurantId = restaurantId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
