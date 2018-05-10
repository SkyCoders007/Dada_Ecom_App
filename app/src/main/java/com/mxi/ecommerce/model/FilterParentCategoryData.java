package com.mxi.ecommerce.model;

import java.util.ArrayList;

/**
 * Created by sonali on 18/12/17.
 */

public class FilterParentCategoryData {

    private String Name;
    private ArrayList<FilterSubChildCategoryData> Items;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ArrayList<FilterSubChildCategoryData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<FilterSubChildCategoryData> Items) {
        this.Items = Items;


    }
}
