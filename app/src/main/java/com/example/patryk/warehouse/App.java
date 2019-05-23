package com.example.patryk.warehouse;

import android.app.Application;

import com.example.patryk.warehouse.REST.Rest;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Rest.init();
    }
}
