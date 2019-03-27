package com.example.patryk.warehouse.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.patryk.warehouse.Objects.Order;
import com.example.patryk.warehouse.Objects.OrderedProduct;
import com.example.patryk.warehouse.Objects.ProductToTake;
import com.example.patryk.warehouse.R;

import java.util.ArrayList;
import java.util.List;

public class TakeProductRecyclerViewAdapter extends RecyclerView.Adapter<TakeProductRecyclerViewAdapter.ViewHolder> {

    private List<ProductToTake> products;
    private double values[];
    private Context context;
    public TakeProductRecyclerViewAdapter(List<ProductToTake> products, Context context) {
        this.products = products;
        this.context = context;

        values = new double[products.size()];
        for(int i = 0; i < products.size();i++){
            values[i] = 0.0;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.take_product_row,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final ProductToTake product = products.get(i);
        viewHolder.product_name.setText(product.getProduct().getProduct().getName());
        viewHolder.productCount.setText(String.valueOf(product.getCountOnLocation()) + " szt");
        viewHolder.toTake.setText("Do wzięcia " + String.valueOf(product.getProduct().getCount()) + " szt");

        viewHolder.edit_productCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    String text = s.toString();
                    double value = Double.valueOf(text);
                    values[i] = value;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public double[] getValues() {
        return values;
    }

    @Override
    public int getItemCount() {
        if(products != null){
            return products.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView product_name;
        EditText edit_productCount;
        TextView productCount;
        TextView toTake;

        public ViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View v){
            product_name = v.findViewById(R.id.tp_product_name);
            edit_productCount = v.findViewById(R.id.tp_edit_productCount);
            productCount = v.findViewById(R.id.tp_productCount);
            toTake = v.findViewById(R.id.tp_to_take);
        }
    }

}



