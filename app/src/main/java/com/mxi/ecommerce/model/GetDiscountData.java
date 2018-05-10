package com.mxi.ecommerce.model;


import java.util.ArrayList;

public class GetDiscountData extends ArrayList<GetDiscountData> {

    String imageUrl;
    String GetDiscountProductname;
    int Productid;


    public String getImageUrl() {
        return imageUrl;
    }

    public String setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return imageUrl;
    }

    public String getGetDiscountProductname() {
        return GetDiscountProductname;
    }

    public void setGetDiscountProductname(String getDiscountProductname) {
        GetDiscountProductname = getDiscountProductname;
    }

    public int getProductid() {
        return Productid;
    }

    public void setProductid(int productid) {
        Productid = productid;
    }

}

