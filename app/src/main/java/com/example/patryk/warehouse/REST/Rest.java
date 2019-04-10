package com.example.patryk.warehouse.REST;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Rest {
    private static RestInterface serviceRest;

    private static OkHttpClient okHttpClient;

    private static Gson gson;

    public static String token = "";

    private Rest() {
    }

    public static RestInterface getRest() {
        return serviceRest;
    }

    public static void init() {

        gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        okHttpClient = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("https://mpsdistribution.herokuapp.com")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build();


        serviceRest = retrofit.create(RestInterface.class);
    }

}
