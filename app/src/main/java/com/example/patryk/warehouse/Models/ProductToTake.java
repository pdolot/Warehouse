package com.example.patryk.warehouse.Models;

import java.io.Serializable;

public class ProductToTake implements Serializable {
    private SerializedProduct product;
    private int count;
    private int tookCount = 0;
    private boolean sendToServer = false;

    public ProductToTake(SerializedProduct product, int count) {
        this.product = product;
        this.count = count;
    }

    public ProductToTake() {
    }

    public SerializedProduct getProduct() {
        return product;
    }

    public void setProduct(SerializedProduct product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTookCount() {
        return tookCount;
    }

    public void setTookCount(int tookCount) {
        this.tookCount = tookCount;
    }

    public boolean isSendToServer() {
        return sendToServer;
    }

    public void setSendToServer(boolean sendToServer) {
        this.sendToServer = sendToServer;
    }
}
