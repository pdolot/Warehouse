package com.example.patryk.warehouse.Objects;

public class ProductToTake {
    private OrderedProduct product;
    private double countOnLocation;

    public ProductToTake(OrderedProduct product, double countOnLocation) {
        this.product = product;
        this.countOnLocation = countOnLocation;
    }

    public OrderedProduct getProduct() {
        return product;
    }

    public void setProduct(OrderedProduct product) {
        this.product = product;
    }

    public double getCountOnLocation() {
        return countOnLocation;
    }

    public void setCountOnLocation(double countOnLocation) {
        this.countOnLocation = countOnLocation;
    }
}
