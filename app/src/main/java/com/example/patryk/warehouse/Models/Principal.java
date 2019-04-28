package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Principal implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("companyName")
    private String name;

    @SerializedName("address")
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
