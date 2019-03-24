package com.example.patryk.warehouse.Objects;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class Order {
    private long id;
    private String recipient;
    private String targetLocation;
    private List<OrderedProduct> products;
    private Date orderDate;
    private Date departureDate;
    private int rampNumber;
    private double numberOfPallets;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }

    public List<OrderedProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderedProduct> products) {
        this.products = products;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public int getRampNumber() {
        return rampNumber;
    }

    public void setRampNumber(int rampNumber) {
        this.rampNumber = rampNumber;
    }

    public double getNumberOfPallets() {
        return numberOfPallets;
    }

    public void setNumberOfPallets(double numberOfPallets) {
        this.numberOfPallets = numberOfPallets;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
