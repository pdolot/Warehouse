package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable {

    @SerializedName("id")
    private Long ID;

    @SerializedName("barCodeLocation")
    private String barCodeLocation;

    @SerializedName("products")
    private List<SerializedProduct> products;

    public List<SerializedProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SerializedProduct> products) {
        this.products = products;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getBarCodeLocation() {
        return barCodeLocation;
    }

    public void setBarCodeLocation(String barCodeLocation) {
        this.barCodeLocation = barCodeLocation;
    }
}
