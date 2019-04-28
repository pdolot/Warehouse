package com.example.patryk.warehouse.Fragments.ViewPagerFragments.Supply;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.patryk.warehouse.Adapters.PalletProductsRecyclerViewAdapter;
import com.example.patryk.warehouse.Fragments.ScannerSpreading;
import com.example.patryk.warehouse.Dialogs.InsertLocationDialog;
import com.example.patryk.warehouse.Models.Location;
import com.example.patryk.warehouse.Models.OrderedProduct;
import com.example.patryk.warehouse.Models.SerializedProduct;
import com.example.patryk.warehouse.Models.SpreadingProduct;
import com.example.patryk.warehouse.Models.Status;
import com.example.patryk.warehouse.Models.Supply;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.REST.Rest;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PalletProductsFragment extends SupplyBaseFragment {
    private static String SUPPLY_CODE = "SUPPLY";
    private static String PALLET_ID = "PALLET_ID";
    public static String PRODUCT_RESULT_CODE = "";
    public static int PRODUCT_ID = -1;

    public static PalletProductsFragment newInstance(Supply supply, int pallet_id) {
        Bundle args = new Bundle();
        args.putSerializable(SUPPLY_CODE, supply);
        args.putInt(PALLET_ID, pallet_id);
        PalletProductsFragment fragment = new PalletProductsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Supply supply;
    private int pallet_id = 0;

    private RecyclerView recyclerView;
    private PalletProductsRecyclerViewAdapter adapter;
    private List<OrderedProduct> products = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pallet_products_fragment, container, false);

        supply = (Supply) getArguments().getSerializable(SUPPLY_CODE);
        pallet_id = getArguments().getInt(PALLET_ID);
        findViews(view);
        setAdapter();
        setListeners();
        return view;
    }

    private void findViews(View v) {
        recyclerView = v.findViewById(R.id.ppf_recyclerView);
    }

    private void setAdapter() {
        products = supply.getPaletts().get(pallet_id).getUsedProducts();
        adapter = new PalletProductsRecyclerViewAdapter(products, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setListeners() {
        adapter.setOnClickListener(new PalletProductsRecyclerViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if(products.get(position).getCount() != 0){
                    switch (v.getId()) {
                        case R.id.ppi_scanner:
                            changeFragment(ScannerSpreading.newInstance(position), true);
                            break;
                        case R.id.ppi_input:
                            PRODUCT_ID = position;
                            showDialog();
                            break;
                    }
                }
            }
        });
    }

    private void showDialog() {
        final InsertLocationDialog dialog = new InsertLocationDialog(getContext());
        dialog.show();
        dialog.setLocation(PRODUCT_RESULT_CODE);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ild_accept:
                        PRODUCT_RESULT_CODE = dialog.getLocation();
                        spreadProduct();
                        dialog.dismiss();
                        break;
                    case R.id.ild_cancel:
                        dialog.dismiss();
                        PRODUCT_RESULT_CODE = "";
                        break;

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PRODUCT_RESULT_CODE.equals("")) {
            showDialog();
        }
    }

    private void spreadProduct(){
        final SerializedProduct product = new SerializedProduct();
        product.setProduct(products.get(PRODUCT_ID).getProduct());
        product.setState(products.get(PRODUCT_ID).getCount());
        List<SerializedProduct> serializedProducts = new ArrayList<>();
        serializedProducts.add(product);

        Location location = new Location();
        location.setBarCodeLocation(PRODUCT_RESULT_CODE);
        location.setProducts(serializedProducts);
        List<Location> locations = new ArrayList<>();
        locations.add(location);

        SpreadingProduct spreadingProduct = new SpreadingProduct();
        spreadingProduct.setBarCode(supply.getPaletts().get(pallet_id).getBarCode());
        spreadingProduct.setLocations(locations);

        System.out.println(PRODUCT_ID + "\n" + pallet_id +" " + new GsonBuilder().setPrettyPrinting().create().toJson(spreadingProduct));
        Rest.getRest().spreadProduct(Rest.token,spreadingProduct).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Status status = response.body();
                    System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                    if(!status.getStatus().equals("error")){
                        products.get(PRODUCT_ID).setTookCount(products.get(PRODUCT_ID).getCount());
                        adapter.notifyDataSetChanged();
                        if(checkIfPalletIsDone()){
                            supply.getPaletts().get(pallet_id).setDone(true);
                        }
                    }else{
                        Toast.makeText(getContext(), "Błędna lokalizacja", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
        PRODUCT_RESULT_CODE = "";
    }

    private boolean checkIfPalletIsDone(){

        for (OrderedProduct product : products) {
            if(product.getTookCount() != product.getCount()){
                return false;
            }
        }
        return true;
    }
}
