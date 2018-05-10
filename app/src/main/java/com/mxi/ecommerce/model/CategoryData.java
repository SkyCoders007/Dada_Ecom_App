package com.mxi.ecommerce.model;

public class CategoryData {

    String name;


    int image;

    public CategoryData(String name, int image) {
        this.name = name;

        this.image = image;
    }

    public String getName() {
        return name;
    }


    public int getImage() {
        return image;
    }

}