package com.mxi.ecommerce.model;


import android.graphics.Bitmap;

import java.util.ArrayList;

public class NewPopularCatelogData extends ArrayList<NewPopularCatelogData> {


    Bitmap imageBitmap;
    String imageUrl;
    int Productid;


    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return imageUrl;
    }
    public int getProductid() {
        return Productid;
    }

    public void setProductid(int productid) {
        Productid = productid;
    }
}
