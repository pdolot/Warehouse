package com.example.patryk.warehouse.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patryk.warehouse.Models.Location;
import com.example.patryk.warehouse.Models.OrderedProduct;
import com.example.patryk.warehouse.Models.Product;
import com.example.patryk.warehouse.Models.SerializedProduct;
import com.example.patryk.warehouse.R;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SerializedProductsRecyclerViewAdapter extends RecyclerView.Adapter<SerializedProductsRecyclerViewAdapter.ViewHolder> {

    private List<SerializedProduct> products;
    private int quantityInPackage;
    private Context context;
    public SerializedProductsRecyclerViewAdapter(List<SerializedProduct> products, Context context) {
        this.products = products;
        this.context = context;
    }

    public void setQuantityInPackage(int quantityInPackage) {
        if(quantityInPackage == 0){
            this.quantityInPackage = 1;
        }else{
            this.quantityInPackage = quantityInPackage;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.serialized_product_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if(i == products.size() - 1){
            viewHolder.separator.setVisibility(View.GONE);
        }

        SerializedProduct sp = products.get(i);
        viewHolder.productCount.setText(sp.getState() + " szt. / " +  sp.getState()/quantityInPackage + " zgrz.");

        if(sp.getExpiryDate() != null){
            viewHolder.productDate.setText(sp.getExpiryDate().substring(0,10));
        }else{
            viewHolder.productDate.setText("-");
        }


        String locations = "LOK: ";
        for(Location l : sp.getLocations()){
            locations += l.getBarCodeLocation() + ", ";
        }

        viewHolder.productLocations.setText(locations.substring(0,locations.length()-2));
    }

    @Override
    public int getItemCount() {
        if(products != null){
            return products.size();
        }
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView productDate;
        TextView productLocations;
        TextView productCount;
        View separator;

        public ViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View v){
            productDate = v.findViewById(R.id.s_product_expiryDate);
            productLocations = v.findViewById(R.id.s_product_locations);
            productCount = v.findViewById(R.id.s_product_count);
            separator = v.findViewById(R.id.separator);
        }
    }

}



