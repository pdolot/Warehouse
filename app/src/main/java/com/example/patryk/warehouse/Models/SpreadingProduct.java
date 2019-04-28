package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpreadingProduct {
    @SerializedName("barCode")
    private String barCode;

    @SerializedName("locations")
    private List<Location> locations;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
