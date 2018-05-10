package com.mxi.ecommerce.model;

/**
 * Created by sonali on 15/3/18.
 */

public class ProductSize {

    public String getProduct_size_name() {
        return product_size_name;
    }

    public void setProduct_size_name(String product_size_name) {
        this.product_size_name = product_size_name;
    }

    public String getProductsize_option_value_id() {
        return productsize_option_value_id;
    }

    public void setProductsize_option_value_id(String productsize_option_value_id) {
        this.productsize_option_value_id = productsize_option_value_id;
    }

    public String getProduct_option_value_id() {
        return product_option_value_id;
    }

    public void setProduct_option_value_id(String product_option_value_id) {
        this.product_option_value_id = product_option_value_id;
    }

    public String getProduct_size_price() {
        return product_size_price;
    }

    public void setProduct_size_price(String product_size_price) {
        this.product_size_price = product_size_price;
    }

    public String getPrice_prefix() {
        return price_prefix;
    }

    public void setPrice_prefix(String price_prefix) {
        this.price_prefix = price_prefix;
    }

    String product_size_name;
    String productsize_option_value_id;
    String product_option_value_id;
    String product_size_price;
    String price_prefix;


}
