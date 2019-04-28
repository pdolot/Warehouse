package com.example.patryk.warehouse.Fragments.ViewPagerFragments.Search;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patryk.warehouse.Adapters.AutoCompleteArrayAdapter;
import com.example.patryk.warehouse.Adapters.SerializedProductsRecyclerViewAdapter;
import com.example.patryk.warehouse.Fragments.Scanner;
import com.example.patryk.warehouse.Models.Id;
import com.example.patryk.warehouse.Models.Product;
import com.example.patryk.warehouse.Models.SerializedProduct;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.REST.Rest;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends SearchBaseFragment implements View.OnClickListener, TextWatcher {

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public static String SEARCH_RESULT_CODE = "";

    private ImageView scanner, searchButton;
    private AutoCompleteTextView search;
    private List<Product> products = new ArrayList<>();
    private AutoCompleteArrayAdapter autoCompleteArrayAdapter;

    private List<SerializedProduct> searchedProducts = new ArrayList<>();
    private RecyclerView recyclerView;
    private SerializedProductsRecyclerViewAdapter adapter;

    private TextView productExist;

    private ImageView productCategory;
    private TextView productName;
    private TextView productStaticLocation;
    private TextView productCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetch_data();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        findViews(view);
        setSerializedProductAdapter();
        setListeners();
        setAdapter();
        return view;
    }

    private void findViews(View v) {
        scanner = v.findViewById(R.id.sf_scanner);
        search = v.findViewById(R.id.sf_search);
        recyclerView = v.findViewById(R.id.sf_recyclerview);

        productExist = v.findViewById(R.id.productExist);
        productCategory = v.findViewById(R.id.sf_productCategory);
        productName = v.findViewById(R.id.sf_productName);
        productStaticLocation = v.findViewById(R.id.sf_productLocation);
        productCount = v.findViewById(R.id.sf_productCount);
        searchButton = v.findViewById(R.id.sf_seach_button);
    }

    private void setListeners() {
        scanner.setOnClickListener(this);
        search.addTextChangedListener(this);
        searchButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sf_scanner:
                changeFragment(Scanner.newInstance("Search"), true);
                break;
            case R.id.sf_seach_button:
                search();
                break;
        }
    }

    private void setAdapter() {
        autoCompleteArrayAdapter = new AutoCompleteArrayAdapter(getContext());
        search.setDropDownBackgroundResource(R.drawable.search_dropitems);
        autoCompleteArrayAdapter.setProductsList(products);
        search.setAdapter(autoCompleteArrayAdapter);
        search.setThreshold(1);
        autoCompleteArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if(autoCompleteArrayAdapter.getCount() > 0 && search.getText().length() > 0){
                    search.setBackground(getResources().getDrawable(R.drawable.search_drop));
                }else{
                    search.setBackground(getResources().getDrawable(R.drawable.search));
                }
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    search.setBackground(getResources().getDrawable(R.drawable.search));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search.setBackground(getResources().getDrawable(R.drawable.search,null));
            }
        });
    }

    private void setSerializedProductAdapter() {
        adapter = new SerializedProductsRecyclerViewAdapter(searchedProducts, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        for (Product p : products) {
            if (p.getBarCode().equals(SEARCH_RESULT_CODE)) {
                search.setText(p.getProducer() + ", " + p.getName());
            }
        }
        search.requestFocus();
        search.setSelection(search.getText().length());

        //products.clear();
    }

    private void fetch_data() {

        Rest.getRest().getProducts(Rest.token).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    products.addAll(response.body());
                    //autoCompleteArrayAdapter.notifyDataSetChanged();
                    //setAdapter();
                    //autoCompleteArrayAdapter.setProductsList(products);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetch_product(Long id) {

        Id idd = new Id();
        idd.setId(id);
        Rest.getRest().getProduct(Rest.token, idd).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product p = response.body();
                    setProductLabels(p);
                    searchedProducts.clear();
                    searchedProducts.addAll(p.getSerializedProducts());
                    adapter.setQuantityInPackage(p.getQuantityInPackage());
                    adapter.notifyDataSetChanged();

                    if (productExist.getVisibility() != View.GONE) {
                        productExist.setVisibility(View.GONE);
                    }
                }else {
                    System.out.println("Blad");
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });
    }

    private void setProductLabels(Product p) {
        setProductCategory(p.getCategory());
        productName.setText(p.getProducer() + " " + p.getName());
        productStaticLocation.setText("LOK: " + p.getLocation().getBarCodeLocation());
        int packageCount = 1;

        if (p.getQuantityInPackage() != 0) {
            packageCount = p.getLogicState() / p.getQuantityInPackage();
        }

        productCount.setText(p.getLogicState() + " szt. / " + packageCount + " zgrz.");
    }

    private void setProductCategory(String category) {
        if(category.equals("Piwo")){
            productCategory.setImageResource(R.drawable.ic_beer);
        }else if(category.equals("Wino")){
            productCategory.setImageResource(R.drawable.ic_wine);
        }else if(category.equals("Wódka")){
            productCategory.setImageResource(R.drawable.ic_vodka);
        }else if(category.equals("Wody")){
            productCategory.setImageResource(R.drawable.ic_water);
        }else if(category.equals("Małe soki")){
            productCategory.setImageResource(R.drawable.ic_juice_s);
        }else if(category.equals("Soki (Karton)")){
            productCategory.setImageResource(R.drawable.ic_juice_k);
        }else if(category.equals("Soki PET")){
            productCategory.setImageResource(R.drawable.ic_juice_b);
        }
    }

    // text change listener

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        search();
    }

    private void search(){
        if (search.getText().toString().contains(",")) {
            String[] searchSplit = search.getText().toString().split(", ");
            if(searchSplit.length == 2){
                for (Product product : products) {
                    if (product.getProducer().equals(searchSplit[0]) && product.getName().equals(searchSplit[1])) {
                        fetch_product(product.getId());
                    }
                }
            }
        }
    }
}
