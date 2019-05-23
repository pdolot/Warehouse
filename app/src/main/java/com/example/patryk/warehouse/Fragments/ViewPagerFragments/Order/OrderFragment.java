package com.example.patryk.warehouse.Fragments.ViewPagerFragments.Order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patryk.warehouse.Adapters.ProductsRecyclerViewAdapter;
import com.example.patryk.warehouse.Dialogs.TakeProductDialog;
import com.example.patryk.warehouse.Fragments.Scanner;
import com.example.patryk.warehouse.Models.Id;
import com.example.patryk.warehouse.Models.Location;
import com.example.patryk.warehouse.Models.Order;
import com.example.patryk.warehouse.Models.OrderedProduct;
import com.example.patryk.warehouse.Models.Product;
import com.example.patryk.warehouse.Models.ProductToTake;
import com.example.patryk.warehouse.Models.SerializedProduct;
import com.example.patryk.warehouse.Models.Status;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.REST.Rest;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends OrderBaseFragment implements
        View.OnClickListener, View.OnTouchListener {

    public static String ORDER_RESULT_CODE = "";
    private static String ORDER = "ORDER";

    private ExpandableRelativeLayout orderInfo;
    private LinearLayout toolBar, cancelOrder;
    private TextView cancelOrderText;
    private ImageView cancelOrderIcon;

    private LinearLayout input_layout, open_input;
    private ImageView barCode_button, open_icon;
    private EditText inputCode;

    private boolean inputIsOpen = false;
    private Location location;
    private float yStart;

    private RecyclerView rv_products;
    private ProductsRecyclerViewAdapter viewAdapter;
    private List<OrderedProduct> productList = new ArrayList<>();
    private List<ProductToTake> tookProduct = new ArrayList<>();

    private static Order order;
    private TextView recipient, address, o_id, departureDate, pallets, postitions;

    private TextView tookProductCount;
    private int tookProduct_count = 0;
    //private static Bundle savedInstance;

    public static OrderFragment newInstance(Order order) {
        Bundle args = new Bundle();
        args.putSerializable(ORDER, order);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fetch_data(getArguments().getLong(ORDER_ID));
        order = (Order) getArguments().getSerializable(ORDER);
        productList.addAll(order.getProducts());
        getUsedProducts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        findViews(view);
        setOrderInfo();
        setTookProductCount();
        setListeners();
        setAdapter();
        return view;
    }

    private void setTookProductCount() {
        int counter = 0;
        int tookCounter = 0;
        for (OrderedProduct p : productList) {
            if (p.getCount() == 0) {
                counter++;
            }
            if(p.getTookCount() > 0){
                tookCounter++;
            }
        }

        tookProduct_count = tookCounter;

        if (counter == productList.size() && productList.size() > 0) {
            orderInfo.setBackgroundColor(getResources().getColor(R.color.possitive, null));
            toolBar.setBackground(getResources().getDrawable(R.drawable.toolbar_pos, null));
            cancelOrderText.setText("ZAKOŃCZ ZAMÓWIENIE");
            cancelOrderIcon.setImageResource(R.drawable.ic_accept);
            //cancelOrder.setBackground(getResources().getDrawable(R.drawable.button_possitive,null));
        } else {
            orderInfo.setBackgroundColor(getResources().getColor(R.color.primary, null));
            toolBar.setBackground(getResources().getDrawable(R.drawable.toolbar, null));
            cancelOrderText.setText("PRZERWIJ ZAMÓWIENIE");
            cancelOrderIcon.setImageResource(R.drawable.ic_remove);
            //cancelOrder.setBackground(getResources().getDrawable(R.drawable.button_danger,null));
        }
        tookProductCount.setText(String.valueOf(counter) + " / " + String.valueOf(productList.size()));
    }

    private void showDialog() {
        if (location.getProducts() != null) {
            // get products on this location and check
            // create list of product in this location
            final List<ProductToTake> productToTake = new ArrayList<>();
            List<SerializedProduct> productsOnLocation = new ArrayList<>();
            productsOnLocation.addAll(location.getProducts());

            // checking if on scanned location some product exist in order
            for (OrderedProduct p : productList) {
                if(p.getCount() > 0){
                    for (SerializedProduct productOnLocation : productsOnLocation) {
                        if(productOnLocation.getProduct().getId() == p.getProduct().getId()){
                            productToTake.add(new ProductToTake(productOnLocation, p.getCount()));
                        }
                    }
                }
            }

            if (productToTake.size() > 0) {
                final TakeProductDialog productDialog = new TakeProductDialog(getContext(), ORDER_RESULT_CODE, true);
                productDialog.setProductToTakes(productToTake);
                productDialog.show();
                productDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.tpd_accept:
                                boolean isOk = true;
                                for (OrderedProduct p : productList) {
                                    int tookCount = 0;
                                    for (ProductToTake t : productToTake) {
                                        //System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(t));
                                        if (p.getProduct().getName().equals(t.getProduct().getProduct().getName())
                                                && p.getProduct().getProducer().equals(t.getProduct().getProduct().getProducer())) {
                                            tookCount += t.getTookCount();
                                        }
                                    }

                                    if (tookCount > p.getCount()) {
                                        isOk = false;
                                        Toast.makeText(getContext(), "Nie możesz wziąć więcej", Toast.LENGTH_SHORT).show();
                                    } else {
                                        p.setTookCount(p.getTookCount() + tookCount);
                                        p.setCount(p.getCount() - tookCount);
                                        for (ProductToTake t : productToTake) {
                                            if (p.getProduct().getName().equals(t.getProduct().getProduct().getName())
                                                    && p.getProduct().getProducer().equals(t.getProduct().getProduct().getProducer())
                                                    && t.getTookCount() != 0) {
                                                tookProduct.add(t);
                                            }
                                        }
                                    }
                                }
                                if (isOk) {
                                    JsonArray tookProductsToSend = new JsonArray();
                                    for (ProductToTake product : tookProduct) {
                                        if(!product.isSendToServer()){

                                            JSONObject object = new JSONObject();
                                            JSONObject p = new JSONObject();
                                            try {
                                                object.put("orderID",order.getId());
                                                p.put("productID",product.getProduct().getId());
                                                p.put("quantity",product.getTookCount());
                                                object.put("product",p);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            JsonParser parser = new JsonParser();
                                            JsonObject gson = (JsonObject) parser.parse(object.toString());
                                            tookProductsToSend.add(gson);
                                            product.setSendToServer(true);
                                        }
                                    }

                                    if(tookProductsToSend.size() > 0){
                                        completeProduct(tookProductsToSend);
                                        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(tookProductsToSend));
                                        Toast.makeText(getContext(), "Wysyłam na serwer", Toast.LENGTH_SHORT).show();
                                    }
                                    viewAdapter.notifyDataSetChanged();
                                    setTookProductCount();
                                    productDialog.dismiss();
                                    closeInput();
                                }
                                break;
                            case R.id.tpd_cancel:
                                productDialog.dismiss();
                                ORDER_RESULT_CODE = "";
                                break;
                        }
                    }
                });
            }
        }
    }

    private void mergeTookProducts() {
        if (tookProduct.size() > 1) {
            for (int i = 0; i < tookProduct.size(); i++) {
                for (int j = tookProduct.size() - 1; j > i; j--) {
                    if (tookProduct.get(i).getProduct().equals(tookProduct.get(j).getProduct())) {
                        int tCount = tookProduct.get(i).getTookCount();
                        tookProduct.get(i).setTookCount(tCount + tookProduct.get(j).getTookCount());
                        tookProduct.remove(j);
                    }
                }
            }
        }
    }

    private void findViews(View v) {
        orderInfo = v.findViewById(R.id.o_erl_orderInfo);
        toolBar = v.findViewById(R.id.o_toolbar);
        barCode_button = v.findViewById(R.id.of_bar_code);
        input_layout = v.findViewById(R.id.of_input_layout);
        open_input = v.findViewById(R.id.of_open_input);
        open_icon = v.findViewById(R.id.of_open_icon);
        rv_products = v.findViewById(R.id.of_rv_products);
        cancelOrder = v.findViewById(R.id.of_cancelOrder);
        inputCode = v.findViewById(R.id.of_inputCode);
        tookProductCount = v.findViewById(R.id.of_took_product);

        //info
        recipient = v.findViewById(R.id.of_orderRecipient);
        address = v.findViewById(R.id.of_orderTargetLocation);
        o_id = v.findViewById(R.id.of_orderId);
        departureDate = v.findViewById(R.id.of_orderDepartureDate);
        pallets = v.findViewById(R.id.of_orderPallets);
        postitions = v.findViewById(R.id.of_orderPositions);

        cancelOrderIcon = v.findViewById(R.id.of_cancelOrderIcon);
        cancelOrderText = v.findViewById(R.id.of_cancelOrderText);
    }

    private void setListeners() {
        toolBar.setOnClickListener(this);
        open_input.setOnClickListener(this);
        rv_products.setOnTouchListener(this);
        cancelOrder.setOnClickListener(this);
        barCode_button.setOnClickListener(this);
        pallets.setOnClickListener(this);
    }

    private void setAdapter() {
        viewAdapter = new ProductsRecyclerViewAdapter(productList, getContext());
        rv_products.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_products.setAdapter(viewAdapter);
        viewAdapter.setOnClickListener(new ProductsRecyclerViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (v.getId()) {
                    case R.id.surface_view:
                        break;
                    case R.id.bottom_view:
                        if (productList.get(position).getTookCount() != 0) {
                            mergeTookProducts();
                            JsonArray tookProductsToReturn = new JsonArray();
                            for (int i = tookProduct.size() - 1; i >= 0 ; i--) {
                                if(tookProduct.get(i).getProduct().getProduct().getId() == productList.get(position).getProduct().getId()){
                                    JSONObject object = new JSONObject();
                                    JSONObject p = new JSONObject();
                                    try {
                                        object.put("orderID",order.getId());
                                        p.put("productID",tookProduct.get(i).getProduct().getId());
                                        //object.put("quantity",tookProduct.get(i).getTookCount());
                                        object.put("product",p);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    JsonParser parser = new JsonParser();
                                    JsonObject gson = (JsonObject) parser.parse(object.toString());
                                    tookProductsToReturn.add(gson);
                                    tookProduct.remove(i);
                                }
                            }

                            if(tookProductsToReturn.size() > 0){
                                returnProduct(tookProductsToReturn);
                                System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(tookProductsToReturn));
                                Toast.makeText(getContext(), "Odkładam produkty", Toast.LENGTH_SHORT).show();
                            }

                            productList.get(position).setTookCount(0);
                            productList.get(position).setCount(productList.get(position).getOrderedQuantity());
                            viewAdapter.notifyDataSetChanged();
                            setTookProductCount();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // toolbar (how many products we took)
            case R.id.o_toolbar:
                orderInfo.toggle();
                break;
            // layout to insert location code
            case R.id.of_open_input:
                if (inputIsOpen) {
                    closeInput();
                } else {
                    openInput();
                }
                break;
            // cancel order
            case R.id.of_cancelOrder:
                mergeTookProducts();
                setTookProductCount();
                if (tookProduct_count > 0 && tookProduct_count < productList.size()) {
                    Toast.makeText(getContext(), "Musisz odłożyć wszystkie wzięte produkty", Toast.LENGTH_SHORT).show();
                } else if (tookProduct_count == productList.size()) {
                    ORDER_RESULT_CODE = "";
                    endOrder();
                } else if (tookProduct_count == 0) {
                    ORDER_RESULT_CODE = "";
                    cancelOrder();
                }
                break;
            // scanner button
            case R.id.of_bar_code:
                if (inputIsOpen) {
                    ORDER_RESULT_CODE = inputCode.getText().toString();
                    if (!ORDER_RESULT_CODE.equals("")) getInfoAboutLocation();
                } else {
                    changeFragment(Scanner.newInstance("Order"), true);
                }
                break;
            case R.id.of_orderPallets:
                mergeTookProducts();
                for (ProductToTake productToTake : tookProduct) {
                    System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(productToTake));
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.of_rv_products:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        yStart = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!rv_products.canScrollVertically(-1)) {
                            if (yStart - event.getY() < 0) {
                                orderInfo.expand();
                            } else if (yStart - event.getY() > 0) {
                                orderInfo.collapse();
                            }
                        } else {
                            if (orderInfo.isExpanded()) {
                                orderInfo.collapse();
                            }
                        }
                        break;
                }
                break;

        }
        return false;
    }

    private void openInput() {
        input_layout.setVisibility(View.VISIBLE);
        open_icon.setImageResource(R.drawable.ic_arrowleft);
        barCode_button.setBackgroundResource(R.drawable.ic_done);
        inputIsOpen = true;
    }

    private void closeInput() {
        input_layout.setVisibility(View.GONE);
        open_icon.setImageResource(R.drawable.ic_arrowright);
        barCode_button.setBackgroundResource(R.drawable.ic_barcode_background);
        inputIsOpen = false;
    }

    private void setOrderInfo() {
        recipient.setText(order.getRecipient().getName());
        address.setText(order.getRecipient().getAddress());
        o_id.setText(String.valueOf(order.getId()));
        departureDate.setText(String.valueOf(order.getDepartureDate().substring(0, 10)));
        pallets.setText(String.format("%.2f", order.getNumberOfPallets()));
        postitions.setText(String.valueOf(order.getNumberOfProducts()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!ORDER_RESULT_CODE.equals("")) {
            getInfoAboutLocation();
        }
        ORDER_RESULT_CODE = "";
    }

    // REST

    private void cancelOrder() {
        Id id = new Id();
        id.setId(order.getId());
        Rest.getRest().cancelOrder(Rest.token, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Przerywam zamówienie", Toast.LENGTH_SHORT).show();
                    changeFragment(OrdersFragment.newInstance(), false);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    private void getInfoAboutLocation() {
        Location l = new Location();
        l.setBarCodeLocation(ORDER_RESULT_CODE);
        Rest.getRest().getProductsFromLocation(Rest.token, l).enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful() && response.body() != null) {
                    location = response.body();
                    showDialog();
                } else {
                    Toast.makeText(getContext(), "Brak produktów na tej lokalizacji", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Toast.makeText(getContext(), "Brak produktów na tej lokalizacji", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void completeProduct(final JsonArray product){
        Rest.getRest().completeProduct(Rest.token,product).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful() && response.body() != null){
                    Status status = response.body();
                    System.out.println(status.getStatus() + "\t" + status.getMessage() + "\t" + product.toString());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });

    }

    private void returnProduct(final JsonArray product){
        Rest.getRest().returnProduct(Rest.token,product).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful() && response.body() != null){
                    Status status = response.body();
                    System.out.println(status.getStatus() + "\t" + status.getMessage() + " " + product.toString());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }

    private void endOrder(){
        JSONObject object = new JSONObject();
        try {
            object.put("orderID",order.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObject gson = new JsonObject();
        JsonParser parser = new JsonParser();
        gson = (JsonObject) parser.parse(object.toString());

        Rest.getRest().endOrder(Rest.token,gson).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful() && response.body() != null){
                    Status status = response.body();
                    System.out.println(status.getStatus() + "\t" + status.getMessage());
                    if(status.getStatus().equals("OK")){
                        changeFragment(OrdersFragment.newInstance(),false);
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }

    private void getUsedProducts(){
        Id id = new Id();
        id.setId(order.getId());
        Rest.getRest().getUsedProducts(Rest.token,id).enqueue(new Callback<List<SerializedProduct>>() {
            @Override
            public void onResponse(Call<List<SerializedProduct>> call, Response<List<SerializedProduct>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<SerializedProduct> usedProducts = new ArrayList<>();
                    usedProducts.addAll(response.body());
                    if(usedProducts.size() > 0){
                        for (SerializedProduct usedProduct : usedProducts) {
                            tookProduct.add(new ProductToTake(usedProduct,0));
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<SerializedProduct>> call, Throwable t) {

            }
        });
    }
}
