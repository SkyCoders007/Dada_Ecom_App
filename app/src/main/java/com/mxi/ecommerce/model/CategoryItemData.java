package com.mxi.ecommerce.model;

import java.util.ArrayList;

public class CategoryItemData {

    private String Name;
    private String images;
    private ArrayList<CategoryChildData> Items;
    String category_id;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ArrayList<CategoryChildData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<CategoryChildData> Items) {
        this.Items = Items;
    }


    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

}