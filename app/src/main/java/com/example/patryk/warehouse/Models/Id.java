package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

public class Id {
    @SerializedName("id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
