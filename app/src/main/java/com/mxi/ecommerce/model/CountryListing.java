package com.mxi.ecommerce.model;

/**
 * Created by sonali on 1/1/18.
 */

public class CountryListing {

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    String country_name;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    String country_id;

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    String state_id;

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    String state_name;

    public String getState_zone_id() {
        return state_zone_id;
    }

    public void setState_zone_id(String state_zone_id) {
        this.state_zone_id = state_zone_id;
    }

    String state_zone_id;

}
