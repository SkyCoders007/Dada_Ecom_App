package com.mxi.ecommerce.model;

import java.util.ArrayList;

/**
 * Created by aksahy on 24/1/18.
 */

public class AttributeGroups {

    private String attribute_group_id;

    private String name;

    private ArrayList<Attribute> attribute;
//    private Attribute[] attribute;

    public String getAttribute_group_id() {
        return attribute_group_id;
    }

    public void setAttribute_group_id(String attribute_group_id) {
        this.attribute_group_id = attribute_group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Attribute> getAttribute() {
        return attribute;
    }

    public void setAttribute(ArrayList<Attribute> attribute) {
        this.attribute = attribute;
    }

}
