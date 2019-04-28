package com.example.patryk.warehouse.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.patryk.warehouse.Models.OrderedProduct;
import com.example.patryk.warehouse.Models.Product;
import com.example.patryk.warehouse.R;

import java.util.List;

public class PalletProductsRecyclerViewAdapter extends RecyclerView.Adapter<PalletProductsRecyclerViewAdapter.ViewHolder> {

    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnClickListener(ClickListener clickListener){
        PalletProductsRecyclerViewAdapter.clickListener = clickListener;
    }

    private List<OrderedProduct> products;
    private Context context;

    public PalletProductsRecyclerViewAdapter(List<OrderedProduct> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pallete_product_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final OrderedProduct o_product = products.get(i);
        Product product = o_product.getProduct();

        setIcon(product.getCategory(),viewHolder);

        viewHolder.productName.setText(product.getName());
        viewHolder.productLocation.setText("LOK: " + product.getLocation().getBarCodeLocation());
        viewHolder.productCount.setText(String.valueOf(o_product.getCount() - o_product.getTookCount()) + " szt. / "
                + String.valueOf((o_product.getCount() - o_product.getTookCount()) / product.getQuantityInPackage()) + " zgrz.");
        if (o_product.getTookCount() == o_product.getCount()) {
            viewHolder.icon.setImageResource(R.drawable.ic_done);
        }
    }

    @Override
    public int getItemCount() {
        if (products != null) {
            return products.size();
        }
        return 0;
    }

    private void setIcon(String category, ViewHolder v) {
        if (category.equals("Piwo")) {
            v.icon.setImageResource(R.drawable.ic_beer);
        } else if (category.equals("Wino")) {
            v.icon.setImageResource(R.drawable.ic_wine);
        } else if (category.equals("Wódka")) {
            v.icon.setImageResource(R.drawable.ic_vodka);
        } else if (category.equals("Wody")) {
            v.icon.setImageResource(R.drawable.ic_water);
        } else if (category.equals("Małe soki")) {
            v.icon.setImageResource(R.drawable.ic_juice_s);
        } else if (category.equals("Soki (Karton)")) {
            v.icon.setImageResource(R.drawable.ic_juice_k);
        } else if (category.equals("Soki PET")) {
            v.icon.setImageResource(R.drawable.ic_juice_b);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SwipeLayout swipeLayout;
        LinearLayout surfaceView;
        ImageView scanner, input;
        ImageView icon;
        TextView productName;
        TextView productLocation;
        TextView productCount;
        boolean isOpen = false;

        public ViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View v) {
            icon = v.findViewById(R.id.product_icon);
            productName = v.findViewById(R.id.product_name);
            productLocation = v.findViewById(R.id.product_location);
            productCount = v.findViewById(R.id.product_count);
            swipeLayout = v.findViewById(R.id.pd_swipeLayout);
            surfaceView = v.findViewById(R.id.surface_view);
            scanner = v.findViewById(R.id.ppi_scanner);
            input = v.findViewById(R.id.ppi_input);
            swipeLayout.setSwipeEnabled(false);
            surfaceView.setOnClickListener(this);
            scanner.setOnClickListener(this);
            input.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(isOpen){
                swipeLayout.close();
                isOpen = false;
            }else{
                swipeLayout.open();
                isOpen = true;
            }
            clickListener.onItemClick(getAdapterPosition(),v);
        }
    }

}



