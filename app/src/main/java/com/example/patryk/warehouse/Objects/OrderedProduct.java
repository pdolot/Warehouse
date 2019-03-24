package com.example.patryk.warehouse.Objects;

public class OrderedProduct {
    private Product product;
    private double count;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
}
