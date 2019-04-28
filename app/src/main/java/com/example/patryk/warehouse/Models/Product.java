package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product implements Serializable {
    @SerializedName("id")
    private Long id;

    @SerializedName("barCode")
    private String barCode;

    @SerializedName("name")
    private String name;

    @SerializedName("producer")
    private String producer;

    @SerializedName("logicState")
    private int logicState;

    @SerializedName("price")
    private double price;

    @SerializedName("staticLocation")
    private Location location;

    @SerializedName("category")
    private String category;

    @SerializedName("quantityInPackage")
    private int quantityInPackage;

    @SerializedName("quantityOnThePalette")
    private int quantityInThePallete;

    @SerializedName("products")
    private List<SerializedProduct> serializedProducts;

    public String getFullName(){
        return producer + ", " + name;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getLogicState() {
        return logicState;
    }

    public void setLogicState(int logicState) {
        this.logicState = logicState;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantityInPackage() {
        return quantityInPackage;
    }

    public void setQuantityInPackage(int quantityInPackage) {
        this.quantityInPackage = quantityInPackage;
    }

    public int getQuantityInThePallete() {
        return quantityInThePallete;
    }

    public void setQuantityInThePallete(int quantityInThePallete) {
        this.quantityInThePallete = quantityInThePallete;
    }

    public List<SerializedProduct> getSerializedProducts() {
        return serializedProducts;
    }

    public void setSerializedProducts(List<SerializedProduct> serializedProducts) {
        this.serializedProducts = serializedProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != null ? !id.equals(product.id) : product.id != null) return false;
        if (barCode != null ? !barCode.equals(product.barCode) : product.barCode != null)
            return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (producer != null ? !producer.equals(product.producer) : product.producer != null)
            return false;
        return location != null ? location.equals(product.location) : product.location == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (barCode != null ? barCode.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (producer != null ? producer.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
