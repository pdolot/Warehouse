package com.example.patryk.warehouse.REST;

import com.example.patryk.warehouse.Models.Id;
import com.example.patryk.warehouse.Models.Location;
import com.example.patryk.warehouse.Models.LocationsToChange;
import com.example.patryk.warehouse.Models.Order;
import com.example.patryk.warehouse.Models.OrderedProduct;
import com.example.patryk.warehouse.Models.Pallet;
import com.example.patryk.warehouse.Models.Product;
import com.example.patryk.warehouse.Models.SerializedProduct;
import com.example.patryk.warehouse.Models.ServerTime;
import com.example.patryk.warehouse.Models.SignIn;
import com.example.patryk.warehouse.Models.SpreadingProduct;
import com.example.patryk.warehouse.Models.Status;
import com.example.patryk.warehouse.Models.Supply;
import com.example.patryk.warehouse.Models.User;
import com.example.patryk.warehouse.Models.UserWorkDay;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
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
    Call<SignIn> login(@Body SignIn signIn);

    @POST("/Product/getInfoAboutProduct")
    Call<Product> getProduct(@Header("Authorization") String authToken, @Body Id id);

    @GET("/order/findNotEndedAll")
    Call<List<Order>> getOrders(@Header("Authorization") String authToken);

    @POST("/order/getInfoAboutOrder")
    Call<Order> getOrder(@Header("Authorization") String authToken, @Body Id id);

    @POST("/Location/infoAboutLocation")
    Call<Location> getProductsFromLocation(@Header("Authorization") String authToken, @Body Location location);

    @POST("/Supply/getInfoAboutSupply")
    Call<Supply> getSupply(@Header("Authorization") String authToken, @Body Supply supply);

    @POST("/Palette/getInfoAboutPalette")
    Call<Pallet> getProductFromPallet(@Header("Authorization") String authToken, @Body Id id);

    @GET("/order/amountOfNotEndedOrders")
    Call<String> getOrdersCount(@Header("Authorization") String authToken);

    @POST("/order/cancelMakingOrder")
    Call<Void> cancelOrder(@Header("Authorization") String authToken, @Body Id id);

    @GET("/order/checkUserActiveOrder")
    Call<Id> getCurrentOrder(@Header("Authorization") String authToken);

    @POST("/Supply/SpreadingGoods")
    Call<Status> spreadProduct(@Header("Authorization") String authToken, @Body SpreadingProduct spreadingProduct);

    @GET("/order/statsOfOrdersEndedByUser")
    Call<List<UserWorkDay>> getUserWorkDays(@Header("Authorization") String authToken);

    @GET("/client/getTime")
    Call<ServerTime> getServerTime(@Header("Authorization") String authToken);

    @POST("/order/completing")
    Call<Status> completeProduct(@Header("Authorization") String authToken, @Body JsonArray body);

    @POST("/order/returning")
    Call<Status> returnProduct(@Header("Authorization") String authToken, @Body JsonArray body);

    @POST("/order/end")
    Call<Status> endOrder(@Header("Authorization") String authToken, @Body JsonObject body);

    @POST("/order/completedProducts")
    Call<List<SerializedProduct>> getUsedProducts(@Header("Authorization") String authToken, @Body Id id);

    @POST("/Product/changeLocationOfTheProduct")
    Call<Status> changeLocation(@Header("Authorization") String authToken, @Body LocationsToChange locations);
}