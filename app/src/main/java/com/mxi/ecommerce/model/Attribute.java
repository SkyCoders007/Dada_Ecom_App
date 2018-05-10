package com.mxi.ecommerce.model;

/**
 * Created by aksahy on 24/1/18.
 */

public class Attribute {
    private String text;

    private String name;

    private String attribute_id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttribute_id() {
        return attribute_id;
    }

    public void setAttribute_id(String attribute_id) {
        this.attribute_id = attribute_id;
    }

}
