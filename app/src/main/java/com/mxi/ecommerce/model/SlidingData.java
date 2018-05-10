package com.mxi.ecommerce.model;


import android.graphics.Bitmap;

import java.util.ArrayList;

public class SlidingData extends ArrayList<SlidingData> {

    Bitmap imageBitmap;
    String imageUrl;



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
}

