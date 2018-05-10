package com.mxi.ecommerce.model;

/**
 * Created by sonali on 14/12/17.
 */

public class CategoryListingdata {

    public String getSharing_url() {
        return sharing_url;
    }

    public void setSharing_url(String sharing_url) {
        this.sharing_url = sharing_url;
    }

    String sharing_url;
    String Price;
    String product_name;
    String thumb;
    String product_id;
    String special;
    String tax;
    String model;

    boolean isInCart, iswishlist;


    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public boolean isInCart() {
        return isInCart;
    }

    public void setInCart(boolean inCart) {
        isInCart = inCart;
    }

    public boolean isIswishlist() {
        return iswishlist;
    }

    public void setIswishlist(boolean iswishlist) {
        this.iswishlist = iswishlist;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }


    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }


    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

}
