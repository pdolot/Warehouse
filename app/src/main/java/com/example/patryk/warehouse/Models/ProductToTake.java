package com.example.patryk.warehouse.Models;

public class ProductToTake {
    private SerializedProduct product;
    private int count;
    private int tookCount = 0;

    public ProductToTake(SerializedProduct product, int count) {
        this.product = product;
        this.count = count;
    }

    public SerializedProduct getProduct() {
        return product;
    }

    public void setProduct(SerializedProduct product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTookCount() {
        return tookCount;
    }

    public void setTookCount(int tookCount) {
        this.tookCount = tookCount;
    }
}
