package com.example.patryk.warehouse.Fragments.ViewPagerFragments.Order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.patryk.warehouse.Adapters.ProductsRecyclerViewAdapter;
import com.example.patryk.warehouse.Fragments.MainFragment;
import com.example.patryk.warehouse.Fragments.Scanner;
import com.example.patryk.warehouse.Objects.OrderedProduct;
import com.example.patryk.warehouse.Objects.Product;
import com.example.patryk.warehouse.R;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends OrderBaseFragment implements
        View.OnClickListener, View.OnTouchListener {

    public static String ORDER_RESULT_CODE = "";

    private ExpandableRelativeLayout orderInfo;
    private LinearLayout toolBar, cancelOrder;

    private LinearLayout input_layout, open_input;
    private ImageView barCode_button, open_icon;
    private EditText inputCode;

    private boolean inputIsOpen = false;
    private float yStart;

    private RecyclerView rv_products;
    private ProductsRecyclerViewAdapter viewAdapter;
    private List<OrderedProduct> productList = new ArrayList<>();



    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addProduct("wine");
        addProduct("vodka");
        addProduct("beer");
        addProduct("wine");
        addProduct("vodka");
        addProduct("beer");
        addProduct("wine");
        addProduct("vodka");
        addProduct("beer");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        findViews(view);
        setListeners();
        setAdapter();
        return view;
    }

    private void addProduct(String category) {
        Product p = new Product();
        p.setName("Carlo Rossi Sweet Rose, 0.75 BUT");
        p.setLocation("S3R7P13");
        p.setCategory(category);
        OrderedProduct product = new OrderedProduct();
        product.setProduct(p);
        product.setCount(8);
        productList.add(product);
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
    }

    private void setListeners() {
        toolBar.setOnClickListener(this);
        open_input.setOnClickListener(this);
        rv_products.setOnTouchListener(this);
        cancelOrder.setOnClickListener(this);
        barCode_button.setOnClickListener(this);
    }

    private void setAdapter(){
        viewAdapter = new ProductsRecyclerViewAdapter(productList,getContext());
        rv_products.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_products.setAdapter(viewAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.o_toolbar:
                orderInfo.toggle();
                break;
            case R.id.of_open_input:
                if(inputIsOpen){
                    input_layout.setVisibility(View.GONE);
                    open_icon.setImageResource(R.drawable.ic_arrowright);
                    barCode_button.setBackgroundResource(R.drawable.ic_barcode_background);
                    inputIsOpen = false;
                }else{
                    input_layout.setVisibility(View.VISIBLE);
                    open_icon.setImageResource(R.drawable.ic_arrowleft);
                    barCode_button.setBackgroundResource(R.drawable.ic_done);
                    inputIsOpen = true;
                    inputCode.setText(ORDER_RESULT_CODE);
                }
                break;
            //-------
            case R.id.of_cancelOrder:
                changeFragment(OrdersFragment.newInstance(),false);
                break;
            //-------
            case R.id.of_bar_code:
                if(inputIsOpen){

                }else{
                    changeFragment(Scanner.newInstance("Order"),true);
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.of_rv_products:
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        yStart = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(!rv_products.canScrollVertically(-1)){
                            if(yStart - event.getY() < 0){
                                orderInfo.expand();
                            }else if(yStart - event.getY() > 0){
                                orderInfo.collapse();
                            }
                        }else{
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

}
