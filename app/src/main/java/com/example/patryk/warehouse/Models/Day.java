package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

public class Day {
    @SerializedName("date")
    private String date;
    @SerializedName("amountOfPositions")
    private int amountOfPositions;
    @SerializedName("amountOfOrders")
    private int amountOfOrders;
    @SerializedName("palettes")
    private double palettes;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmountOfPositions() {
        return amountOfPositions;
    }

    public void setAmountOfPositions(int amountOfPositions) {
        this.amountOfPositions = amountOfPositions;
    }

    public int getAmountOfOrders() {
        return amountOfOrders;
    }

    public void setAmountOfOrders(int amountOfOrders) {
        this.amountOfOrders = amountOfOrders;
    }

    public double getPalettes() {
        return palettes;
    }

    public void setPalettes(double palettes) {
        this.palettes = palettes;
    }
}
