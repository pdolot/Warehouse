package com.example.patryk.warehouse.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patryk.warehouse.Objects.Order;
import com.example.patryk.warehouse.Objects.OrderedProduct;
import com.example.patryk.warehouse.Objects.Product;
import com.example.patryk.warehouse.R;

import java.util.List;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    private List<OrderedProduct> products;
    private Context context;
    public ProductsRecyclerViewAdapter(List<OrderedProduct> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        OrderedProduct o_product = products.get(i);
        Product product = o_product.getProduct();
        setIcon(product.getCategory(),viewHolder);
        viewHolder.productName.setText(product.getName());
        viewHolder.productLocation.setText(product.getLocation());
        viewHolder.productCount.setText(String.valueOf(o_product.getCount()) + " szt");
    }

    @Override
    public int getItemCount() {
        if(products != null){
            return products.size();
        }
        return 0;
    }

    private void setIcon(String category, ViewHolder v){
        if(category.equals("beer")){
            v.icon.setImageResource(R.drawable.ic_beer);
        }else if(category.equals("wine")){
            v.icon.setImageResource(R.drawable.ic_wine);
        }else if(category.equals("vodka")){
            v.icon.setImageResource(R.drawable.ic_vodka);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView productName;
        TextView productLocation;
        TextView productCount;

        public ViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View v){
            icon = v.findViewById(R.id.product_icon);
            productName = v.findViewById(R.id.product_name);
            productLocation = v.findViewById(R.id.product_location);
            productCount = v.findViewById(R.id.product_count);
        }
    }

}


