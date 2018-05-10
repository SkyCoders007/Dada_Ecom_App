package com.mxi.ecommerce.model;


public class CartDetailData {

    private String cart_image;
    private String title;
    private String total;
    private String sub_total;
    private String cart_id;
    private String name;
    private String models;


    private boolean stock;
    private String price;
    private String quantity;
    private String product_id;


    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }


    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getName() {
        return name;
    }

    public String setName(String name) {
        this.name = name;
        return name;
    }

    public String getModels() {
        return models;
    }

    public String setModels(String models) {
        this.models = models;
        return models;
    }


    public String getPrice() {
        return price;
    }

    public String setPrice(String price) {
        this.price = price;
        return price;
    }

    public String getCart_image() {
        return cart_image;
    }

    public String setCart_image(String cart_image) {
        this.cart_image = cart_image;
        return cart_image;
    }
}
