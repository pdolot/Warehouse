package com.example.patryk.warehouse;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.patryk.warehouse.Adapters.TakeProductRecyclerViewAdapter;
import com.example.patryk.warehouse.Models.ProductToTake;

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
    private RecyclerView recyclerView;
    private List<ProductToTake> productToTakes = new ArrayList<>();
    private TakeProductRecyclerViewAdapter adapter;
    private boolean ordered;
    private CheckBox checkAll;

    public int[] getValues() {
        return adapter.getValues();
    }

    public TakeProductDialog(@NotNull Context context, String location, boolean ordered) {
        super(context);
        this.location = location;
        this.ordered = ordered;
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
        checkAll = findViewById(R.id.tpd_checkAll);
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    for (ProductToTake productToTake : productToTakes) {
                        productToTake.setTookCount(productToTake.getCount());
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    for (ProductToTake productToTake : productToTakes) {
                        productToTake.setTookCount(0);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setAdapter(){
        adapter = new TakeProductRecyclerViewAdapter(productToTakes,getContext(), ordered);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
