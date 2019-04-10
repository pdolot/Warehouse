package com.example.patryk.warehouse.REST;

import com.example.patryk.warehouse.Models.Id;
import com.example.patryk.warehouse.Models.Location;
import com.example.patryk.warehouse.Models.Order;
import com.example.patryk.warehouse.Models.Product;
import com.example.patryk.warehouse.Models.User;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestInterface {
    @GET("/Product/findAllProducts")
    Call<List<Product>> getProducts(@Header("Authorization") String authToken);

    @POST("/auth")
    Call<User> login(@Body User user);

    @POST("/Product/getInfoAboutProduct")
    Call<Product> getProduct(@Header("Authorization") String authToken, @Body Id id);

    @GET("/order/findNotEndedAll")
    Call<List<Order>> getOrders(@Header("Authorization") String authToken);

    @POST("/order/getInfoAboutOrder")
    Call<Order> getOrder(@Header("Authorization") String authToken, @Body Id id);

    @POST("/Location/infoAboutLocation")
    Call<Location> getProductsFromLocation(@Header("Authorization") String authToken, @Body Location location);
}