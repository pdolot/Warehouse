package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Pallet implements Serializable {
    @SerializedName("id")
    private Long id;
    @SerializedName("barCode")
    private String barCode;
    @SerializedName("products")
    private List<OrderedProduct> usedProducts;

    private boolean isDone = false;

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public List<OrderedProduct> getUsedProducts() {
        return usedProducts;
    }

    public void setUsedProducts(List<OrderedProduct> usedProducts) {
        this.usedProducts = usedProducts;
    }
}
