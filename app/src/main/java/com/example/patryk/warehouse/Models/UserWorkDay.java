package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

public class UserWorkDay {

    @SerializedName("Day")
    private Day day;
    private double performance;

    public double getPerformance() {
        return day.getAmountOfPositions()*day.getPalettes();
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
