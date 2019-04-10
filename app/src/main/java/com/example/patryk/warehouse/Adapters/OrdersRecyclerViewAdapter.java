package com.example.patryk.warehouse.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patryk.warehouse.Models.Order;
import com.example.patryk.warehouse.R;

import java.util.List;

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.ViewHolder> {

    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    private List<Order> orders;
    private Context context;
    public OrdersRecyclerViewAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Order order = orders.get(i);
        viewHolder.recipient.setText(order.getRecipient().getName());
        viewHolder.targetLocation.setText(order.getRecipient().getAddress());
    }

    @Override
    public int getItemCount() {
        if(orders != null){
            return orders.size();
        }
        return 0;
    }

    public void setOnClickListener(ClickListener clickListener){
        OrdersRecyclerViewAdapter.clickListener = clickListener;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView recipient;
        TextView targetLocation;
        TextView departureDate_ddMM;
        TextView departureDate_yyyy;

        public ViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
            itemView.setOnClickListener(this);
        }

        private void findViews(View v){
            recipient = v.findViewById(R.id.oi_recipient);
            targetLocation = v.findViewById(R.id.oi_targetLocation);
            departureDate_ddMM = v.findViewById(R.id.oi_date_ddMM);
            departureDate_yyyy = v.findViewById(R.id.oi_date_yyyy);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(),v);
        }
    }

}



