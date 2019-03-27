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
import com.example.patryk.warehouse.Fragments.Scanner;
import com.example.patryk.warehouse.Objects.OrderedProduct;
import com.example.patryk.warehouse.Objects.Product;
import com.example.patryk.warehouse.Objects.ProductToTake;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.TakeProductDialog;
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

    private TextView tookProductCount;

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addProduct("wine", "020501",90);
        addProduct("vodka","030503", 84);
        addProduct("beer","0105012", 16);
        addProduct("wine","020502", 17);
        addProduct("vodka","030502",6);
        addProduct("beer","010803", 20);
        addProduct("wine","010503",36);
        addProduct("vodka","030401",18);
        addProduct("beer","010803",6);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        findViews(view);
        setListeners();
        setAdapter();
        setTookProductCount();
        showDialog();
        return view;
    }

    private void setTookProductCount(){
        int counter = 0;
        for(OrderedProduct p : productList){
            if(p.getCount() == 0){
                counter++;
            }
        }
        tookProductCount.setText(String.valueOf(counter) + " / " + String.valueOf(productList.size()));
    }

    private void showDialog(){
        // need to check if code exist in data base
        if(!ORDER_RESULT_CODE.equals("")){
            // get products on this location and check
            // create list of product in this location
            final List<ProductToTake> productToTake = new ArrayList<>();
            for(OrderedProduct p : productList){
                if(p.getProduct().getLocation().equals(ORDER_RESULT_CODE) && p.getCount() > 0){
                    productToTake.add(new ProductToTake(p,120));
                }
            }

            if(productToTake.size() > 0){
                final TakeProductDialog productDialog = new TakeProductDialog(getContext(),null,ORDER_RESULT_CODE);
                productDialog.setProductToTakes(productToTake);
                productDialog.show();
                productDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.tpd_accept:
                                double values[] = productDialog.getValues();

                                for(int i = 0; i < values.length; i++){
                                    double count = productToTake.get(i).getProduct().getCount();
                                    if(values[i] <= count && values[i] <= 120){
                                        productToTake.get(i).getProduct().setCount(count - values[i]);
                                    }
                                }

                                for(OrderedProduct p : productList){
                                    for(ProductToTake t : productToTake){
                                        if(p == t.getProduct()){
                                            p.setCount(t.getProduct().getCount());
                                        }
                                    }
                                    viewAdapter.notifyDataSetChanged();
                                }
                                setTookProductCount();
                                productDialog.dismiss();
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

    private void addProduct(String category, String location, double count) {
        Product p = new Product();
        p.setName("Carlo Rossi Sweet Rose, 0.75 BUT");
        p.setLocation(location);
        p.setCategory(category);
        OrderedProduct product = new OrderedProduct();
        product.setProduct(p);
        product.setCount(count);
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
        tookProductCount = v.findViewById(R.id.of_took_product);
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
            // toolbar (how many products we took)
            case R.id.o_toolbar:
                orderInfo.toggle();
                break;
             // layout to insert location code
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
                }
                break;
            // cancel order
            case R.id.of_cancelOrder:
                changeFragment(OrdersFragment.newInstance(),false);
                break;
            // scanner button
            case R.id.of_bar_code:
                if(inputIsOpen){
                    ORDER_RESULT_CODE = inputCode.getText().toString();
                    showDialog();
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
