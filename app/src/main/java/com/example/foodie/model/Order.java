package com.example.foodie.model;

public class Order {
    private Integer id;
    private String user;
    private Integer menuId;
    private Integer restaurantId;
    private Integer count;
    private Float price;
    private Boolean isPayed;

    public Order() {
    }

    public Order(Integer id, String user, Integer menuId, Integer restaurantId, Integer count, Float price, Boolean isPayed) {
        this.id = id;
        this.user = user;
        this.menuId = menuId;
        this.restaurantId = restaurantId;
        this.count = count;
        this.price = price;
        this.isPayed = isPayed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean getPayed() {
        return isPayed;
    }

    public void setPayed(Boolean payed) {
        isPayed = payed;
    }
}
