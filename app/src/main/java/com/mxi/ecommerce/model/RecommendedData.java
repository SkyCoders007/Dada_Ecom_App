package com.mxi.ecommerce.model;

import java.util.ArrayList;

public class RecommendedData extends ArrayList<RecommendedData> {

    String imageUrl;
    String model;
    String shareurl;
    String RecommendedName;
    int Productid;
    String Price;
    String special;
    String tax;
    boolean isInCart, iswishlist;

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }



    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

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

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return imageUrl;
    }

    public String getRecommendedName() {
        return RecommendedName;
    }

    public void setRecommendedName(String recommendedName) {
        RecommendedName = recommendedName;
    }


    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }


    public int getProductid() {
        return Productid;
    }

    public void setProductid(int productid) {
        Productid = productid;
    }


}


