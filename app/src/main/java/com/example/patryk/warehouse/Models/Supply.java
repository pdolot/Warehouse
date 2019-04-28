package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Supply implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("typeOfSupply")
    private String typeOfSupply;

    @SerializedName("barCodeOfSupply")
    private String barCodeOfSupply;

    @SerializedName("palletes")
    private List<Pallet> paletts;

    @SerializedName("arriveDate")
    private String arriveDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeOfSupply() {
        return typeOfSupply;
    }

    public void setTypeOfSupply(String typeOfSupply) {
        this.typeOfSupply = typeOfSupply;
    }

    public String getBarCodeOfSupply() {
        return barCodeOfSupply;
    }

    public void setBarCodeOfSupply(String barCodeOfSupply) {
        this.barCodeOfSupply = barCodeOfSupply;
    }

    public List<Pallet> getPaletts() {
        return paletts;
    }

    public void setPaletts(List<Pallet> paletts) {
        this.paletts = paletts;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }
}
