package com.mxi.ecommerce.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class CategoryDataTwo extends ArrayList<CategoryDataTwo> {

    Bitmap imageBitmap;
    String imageUrl;
    String categoryname;
    String categoey_id;


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

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategoey_id() {
        return categoey_id;
    }

    public void setCategoey_id(String categoey_id) {
        this.categoey_id = categoey_id;
    }


}

