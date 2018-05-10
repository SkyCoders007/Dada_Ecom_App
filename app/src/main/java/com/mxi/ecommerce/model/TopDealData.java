package com.mxi.ecommerce.model;


import java.util.ArrayList;

public class TopDealData extends ArrayList<SlidingData> {

    String special;
    String share_url;
    String imageUrl;
    String Topdealname;
    String Productid;
    String Price;
    String model;
    String tax;
    boolean isInCart,iswishlist;

    public boolean isInCart() {
        return isInCart;
    }

    public void setInCart(boolean inCart) {
        isInCart = inCart;
    }

    public boolean isIswishlist() {
        return iswishlist;
    }

    public void setIswishlist(boolean iswishlist) {
        this.iswishlist = iswishlist;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getProductid() {
        return Productid;
    }

    public void setProductid(String productid) {
        Productid = productid;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return imageUrl;
    }

    public String getTopdealname() {
        return Topdealname;
    }

    public void setTopdealname(String topdealname) {
        Topdealname = topdealname;
    }

}

