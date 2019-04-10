package com.example.patryk.warehouse.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.patryk.warehouse.Models.Product;
import com.example.patryk.warehouse.R;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteArrayAdapter extends ArrayAdapter<Product> {

    private List<Product> productsList;


    public AutoCompleteArrayAdapter(@NonNull Context context) {
        super(context, 0);
    }

    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return productFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.autocomplete_row, parent, false
            );
        }

        TextView name = convertView.findViewById(R.id.ac_r_name);

        Product product = getItem(position);

        if (product != null) {
            name.setText(product.getProducer() + ", " + product.getName());
        }

        return convertView;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Product> suggestions = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(productsList);
            } else {
                String filterPattern = constraint.toString().toLowerCase();
                for (Product p : productsList) {
                    if (p.getName().toLowerCase().contains(filterPattern)
                            || p.getBarCode().toLowerCase().contains(filterPattern)
                            || p.getProducer().toLowerCase().contains(filterPattern)) {
                        suggestions.add(p);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Product) resultValue).getProducer() + ", " +((Product) resultValue).getName();
        }
    };
}
