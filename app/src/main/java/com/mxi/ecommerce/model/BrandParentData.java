package com.mxi.ecommerce.model;

import java.util.ArrayList;

/**
 * Created by sonali on 18/12/17.
 */

public class BrandParentData {
    private String Name;

    private ArrayList<CategoryBrandChildData> Items;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ArrayList<CategoryBrandChildData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<CategoryBrandChildData> Items) {
        this.Items = Items;


}

}
