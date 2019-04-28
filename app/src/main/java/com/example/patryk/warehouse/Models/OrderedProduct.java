package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderedProduct implements Serializable {

    @SerializedName("product")
    private Product product;

    @SerializedName("orderedQuantity")
    private int orderedQuantity;

    @SerializedName("quantity")
    private int count;

    @SerializedName("pickedQuantity")
    private int tookCount;

    public int getTookCount() {
        return tookCount;
    }

    public void setTookCount(int tookedCount) {
        this.tookCount = tookedCount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(int orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }
}
