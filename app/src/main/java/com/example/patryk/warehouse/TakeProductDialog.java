package com.example.patryk.warehouse;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.patryk.warehouse.Adapters.TakeProductRecyclerViewAdapter;
import com.example.patryk.warehouse.Objects.OrderedProduct;
import com.example.patryk.warehouse.Objects.ProductToTake;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TakeProductDialog extends Dialog {

    public void setOnClickListener(View.OnClickListener onClickListener){
        accept.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
    }

    private static String LOC = "LOK: ";
    private String location;

    private LinearLayout accept, cancel;
    private TextView locationLabel;
    private OrderedProduct product;
    private RecyclerView recyclerView;
    private List<ProductToTake> productToTakes = new ArrayList<>();
    private TakeProductRecyclerViewAdapter adapter;

    public double[] getValues() {
        return adapter.getValues();
    }

    public TakeProductDialog(@NotNull Context context, OrderedProduct product, String location) {
        super(context);
        this.product = product;
        this.location = location;
    }


    public void setProductToTakes(List<ProductToTake> productToTakes) {
        this.productToTakes = productToTakes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.take_product_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        findViews();
        setAdapter();

        locationLabel.setText(LOC + location);
    }

    private void findViews(){
        accept = findViewById(R.id.tpd_accept);
        cancel = findViewById(R.id.tpd_cancel);
        locationLabel = findViewById(R.id.tpd_location);
        recyclerView = findViewById(R.id.tpd_recyclerView);
    }

    private void setAdapter(){
        adapter = new TakeProductRecyclerViewAdapter(productToTakes,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
