package com.example.nrbaboo.scbtest;

public class CardMobile {

    private int id;
    private  String name;
    private  String detail;
    private  double price;
    private  double rate;
    private  String imageUrl;
    private  boolean favorite;
    private  String brand;


    public CardMobile(int id,String name, String detail, String imageUrl,double price,double rate,boolean favorite,String brand) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.imageUrl = imageUrl;
        this.price = price;
        this.rate = rate;
        this.favorite = favorite;
        this.brand = brand;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
