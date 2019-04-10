package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

public class OrderedProduct {

    @SerializedName("product")
    private Product product;

    @SerializedName("quantity")
    private int count;

    private int tookCount = 0;

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
}
