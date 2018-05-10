package com.mxi.ecommerce.model;

import com.mobisys.android.autocompleteview.annotations.ViewId;
import com.mxi.ecommerce.R;

/**
 * Created by aksahy on 19/2/18.
 */

public class Product {

    private String price;

    private String tax;

    private String product_id;

    private String minimum;

    private String description;

    private String name;

    private String special;

    private String rating;

    private String cat_img_url;

    private String href;

    private String thumb;

    private String review;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ViewId(id= R.id.name)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCat_img_url() {
        return cat_img_url;
    }

    public void setCat_img_url(String cat_img_url) {
        this.cat_img_url = cat_img_url;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "ClassPojo [price = " + price + ", tax = " + tax + ", product_id = " + product_id + ", minimum = " + minimum + ", description = " + description + ", name = " + name + ", special = " + special + ", rating = " + rating + ", cat_img_url = " + cat_img_url + ", href = " + href + ", thumb = " + thumb + ", review = " + review + "]";
    }
}
