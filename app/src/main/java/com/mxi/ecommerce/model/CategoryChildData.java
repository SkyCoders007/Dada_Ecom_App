package com.mxi.ecommerce.model;

/**
 * Created by sonali on 12/12/17.
 */

public class CategoryChildData {



    private String Name;
    private String Image;
    String child_id;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }


    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }




}