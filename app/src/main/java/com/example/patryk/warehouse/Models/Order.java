package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    @SerializedName("id")
    private Long id;

    @SerializedName("principal")
    private Principal recipient;

    @SerializedName("products")
    private List<OrderedProduct> products;

    @SerializedName("departureDate")
    private String departureDate;

    @SerializedName("productsCount")
    private int numberOfProducts;

    @SerializedName("palletes")
    private double numberOfPallets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Principal getRecipient() {
        return recipient;
    }

    public void setRecipient(Principal recipient) {
        this.recipient = recipient;
    }

    public List<OrderedProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderedProduct> products) {
        this.products = products;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    public double getNumberOfPallets() {
        return numberOfPallets;
    }

    public void setNumberOfPallets(double numberOfPallets) {
        this.numberOfPallets = numberOfPallets;
    }
}
