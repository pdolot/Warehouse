package com.example.patryk.warehouse.Fragments.ViewPagerFragments.Order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patryk.warehouse.Adapters.OrdersRecyclerViewAdapter;
import com.example.patryk.warehouse.Models.Id;
import com.example.patryk.warehouse.Models.Order;
import com.example.patryk.warehouse.Models.Principal;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.REST.Rest;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends OrderBaseFragment{

    private RecyclerView orders;
    private List<Order> ordersList = new ArrayList<>();
    private OrdersRecyclerViewAdapter ordersAdapter;
    private TextView ordersExist;
    private SwipeRefreshLayout refreshLayout;

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetch_data(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.orders_fragment,container,false);
        findViews(view);
        setAdapter();
        setOnClickListeners();
        return view;
    }


    private void setOnClickListeners() {
        ordersAdapter.setOnClickListener(new OrdersRecyclerViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Long id = ordersList.get(position).getId();
                fetch_order(id);
                //changeFragment(OrderFragment.newInstance(id),false);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch_data(true);
            }
        });
    }

    private void findViews(View v){
        orders = v.findViewById(R.id.rv_orders);
        ordersExist = v.findViewById(R.id.ordersExist);
        refreshLayout = v.findViewById(R.id.of_swipeLayout);
    }

    private void setAdapter(){
        ordersAdapter = new OrdersRecyclerViewAdapter(ordersList, getContext());
        orders.setLayoutManager(new LinearLayoutManager(getContext()));
        orders.setAdapter(ordersAdapter);
    }

    private void fetch_data(final boolean isRefreshing){

        Rest.getRest().getOrders(Rest.token).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.isSuccessful() && response != null){
                    ordersList.clear();
                    ordersList.addAll(response.body());
                    ordersAdapter.notifyDataSetChanged();
                    if(!ordersList.isEmpty()){
                        ordersExist.setVisibility(View.GONE);
                    }else {
                        ordersExist.setVisibility(View.VISIBLE);
                    }
                }else{
                    ordersExist.setVisibility(View.VISIBLE);
                }

                if(isRefreshing){
                    if(refreshLayout.isRefreshing()){
                        refreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                if(isRefreshing){
                    if(refreshLayout.isRefreshing()){
                        ordersExist.setVisibility(View.VISIBLE);
                        refreshLayout.setRefreshing(false);
                    }
                }
            }
        });

    }

    private void fetch_order(final Long order_id) {
        Id id = new Id();
        id.setId(order_id);
        Rest.getRest().getOrder(Rest.token, id).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response != null) {
                    Order order = response.body();
                    changeFragment(OrderFragment.newInstance(order),false);
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }
}
