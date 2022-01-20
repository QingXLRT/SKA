package com.example.bottomnavigatonview;

/**
 * Created by karanjaswani on 1/12/18.
 */

public class Product {
    private int id;
    private String title;
    private String category;
    private double quantity;
    private String date_of_purchase;
    private int image;

    public Product(int id, String title, String category, double quantity, String date_of_purchase, int image) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.quantity = quantity;
        this.date_of_purchase = date_of_purchase;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public double getQuantity() {
        return quantity;
    }

    public double addQuantity(){return quantity+=1;}

    public String getDateOfPurchase() {return date_of_purchase; }

    public int getImage() {
        return image;
    }
}