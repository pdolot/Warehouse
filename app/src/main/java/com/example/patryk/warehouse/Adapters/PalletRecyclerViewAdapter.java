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
import com.example.patryk.warehouse.Models.Pallet;
import com.example.patryk.warehouse.Models.Product;
import com.example.patryk.warehouse.R;

import java.util.List;

public class PalletRecyclerViewAdapter extends RecyclerView.Adapter<PalletRecyclerViewAdapter.ViewHolder> {

    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnClickListener(ClickListener clickListener) {
        PalletRecyclerViewAdapter.clickListener = clickListener;
    }

    private List<Pallet> pallets;
    private Context context;

    public PalletRecyclerViewAdapter(List<Pallet> pallets, Context context) {
        this.pallets = pallets;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.palletes_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Pallet pallet = pallets.get(i);

        if(pallet.isDone()){
            viewHolder.palletSrc.setImageResource(R.drawable.ic_pallete);
            viewHolder.isDone.setVisibility(View.VISIBLE);
            viewHolder.palleteNo.setVisibility(View.GONE);
        }else{
            viewHolder.palletSrc.setImageResource(R.drawable.ic_pallete_gray);
            viewHolder.isDone.setVisibility(View.GONE);
            viewHolder.palleteNo.setVisibility(View.VISIBLE);
            viewHolder.palleteNo.setText(String.valueOf(i + 1));
        }

    }

    @Override
    public int getItemCount() {
        if (pallets != null) {
            return pallets.size();
        }
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView palletSrc;
        ImageView isDone;
        TextView palleteNo;

        public ViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View v) {
            palletSrc = v.findViewById(R.id.pi_palletSrc);
            isDone = v.findViewById(R.id.pi_palletDone);
            palleteNo = v.findViewById(R.id.pi_palletNo);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

}



