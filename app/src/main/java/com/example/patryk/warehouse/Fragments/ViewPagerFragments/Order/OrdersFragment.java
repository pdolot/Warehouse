package com.example.patryk.warehouse.Fragments.ViewPagerFragments.Order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.patryk.warehouse.Adapters.OrdersRecyclerViewAdapter;
import com.example.patryk.warehouse.Objects.Order;
import com.example.patryk.warehouse.R;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends OrderBaseFragment implements View.OnClickListener{

    private Button tmp;
    private RecyclerView orders;
    private List<Order> ordersList = new ArrayList<>();
    private OrdersRecyclerViewAdapter ordersAdapter;

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addOrder();
        addOrder();
        addOrder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.orders_fragment,container,false);
        findViews(view);
        setAdapter();
        tmp.setOnClickListener(this);
        return view;
    }

    private void findViews(View v){
        orders = v.findViewById(R.id.rv_orders);
        tmp = v.findViewById(R.id.tmp_button);
    }

    private void setAdapter(){
        ordersAdapter = new OrdersRecyclerViewAdapter(ordersList, getContext());
        orders.setLayoutManager(new LinearLayoutManager(getContext()));
        orders.setAdapter(ordersAdapter);
    }

    private void addOrder(){
        Order order = new Order();
        order.setRecipient("Kapselek");
        order.setTargetLocation("Łódź, Limanowskiego 34a");
        ordersList.add(order);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tmp_button:
                changeFragment(OrderFragment.newInstance(),false);
                break;
        }
    }
}
