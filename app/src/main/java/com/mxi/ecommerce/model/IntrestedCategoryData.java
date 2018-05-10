package com.mxi.ecommerce.model;

/**
 * Created by sonali on 14/12/17.
 */

public class IntrestedCategoryData {


    public String getSharing_url() {
        return sharing_url;
    }

    public void setSharing_url(String sharing_url) {
        this.sharing_url = sharing_url;
    }

    String sharing_url;
    String imageUrl;
    String productname;
    String price;
    String product_id;

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    String tax;

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    String special;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    String model;

    public boolean isInCart() {
        return isInCart;
    }

    public void setInCart(boolean inCart) {
        isInCart = inCart;
    }

    boolean isInCart;

    public boolean iswishlist() {
        return iswishlist;
    }

    public void setIswishlist(boolean iswishlist) {
        this.iswishlist = iswishlist;
    }

    boolean iswishlist;



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

}
