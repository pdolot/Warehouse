package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("firstname")
    private String firstName;

    @SerializedName("lastname")
    private String lastName;

    @SerializedName("workingHours")
    private String workingHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public int getStartWorkHour() {
        return Integer.valueOf(workingHours.substring(0,2));
    }

    public int getEndWorkHour() {
        return Integer.valueOf(workingHours.substring(3,5));
    }

    public String getUserWorkHours(){
        return String.format("%02d",this.getStartWorkHour())+":00-"+String.format("%02d",this.getEndWorkHour())+":00";
    }
}
