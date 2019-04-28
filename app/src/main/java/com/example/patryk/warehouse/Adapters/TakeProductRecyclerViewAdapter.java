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

import com.example.patryk.warehouse.Models.ProductToTake;
import com.example.patryk.warehouse.R;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

public class TakeProductRecyclerViewAdapter extends RecyclerView.Adapter<TakeProductRecyclerViewAdapter.ViewHolder> {

    private List<ProductToTake> products;
    private int values[];
    private Context context;
    private boolean ordered;
    public TakeProductRecyclerViewAdapter(List<ProductToTake> products, Context context, boolean ordered) {
        this.products = products;
        this.context = context;
        this.ordered = ordered;

        values = new int[products.size()];
        for(int i = 0; i < products.size();i++){
            values[i] = products.get(i).getCount();
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
        viewHolder.productCount.setText(" / " + String.valueOf(product.getProduct().getState()) + " szt");
        if(ordered){
            viewHolder.toTake.setText("Do wzięcia " + String.valueOf(product.getCount()) + " szt");
        }else{
            viewHolder.toTake.setVisibility(View.GONE);
        }

        if(i == products.size() - 1){
            viewHolder.separator.setVisibility(View.GONE);
        }


        if(product.getProduct().getExpiryDate() != null){
            viewHolder.expiry_date.setText(product.getProduct().getExpiryDate().substring(0,10));
        }

        viewHolder.edit_productCount.setText(String.valueOf(product.getTookCount()));
        viewHolder.edit_productCount.setSelection(viewHolder.edit_productCount.getText().length());

        viewHolder.edit_productCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    String text = s.toString();
                    int value = Integer.valueOf(text);
                    if(value > product.getCount()){
                        viewHolder.edit_productCount.setText(String.valueOf(product.getCount()));
                        viewHolder.edit_productCount.setSelection(viewHolder.edit_productCount.getText().length());
                        value = product.getCount();
                    }
                    product.setTookCount(value);
                }else{
                    viewHolder.edit_productCount.setText("0");
                    viewHolder.edit_productCount.setSelection(1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public int[] getValues() {
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
        TextView expiry_date;
        View separator;

        public ViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View v){
            product_name = v.findViewById(R.id.tp_product_name);
            edit_productCount = v.findViewById(R.id.tp_edit_productCount);
            productCount = v.findViewById(R.id.tp_productCount);
            toTake = v.findViewById(R.id.tp_to_take);
            expiry_date = v.findViewById(R.id.tp_expiryDate);
            separator = v.findViewById(R.id.tp_separator);
        }
    }

}



