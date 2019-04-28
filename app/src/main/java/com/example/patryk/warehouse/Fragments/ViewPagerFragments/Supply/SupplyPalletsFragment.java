package com.example.patryk.warehouse.Fragments.ViewPagerFragments.Supply;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patryk.warehouse.Adapters.PalletRecyclerViewAdapter;
import com.example.patryk.warehouse.Models.Id;
import com.example.patryk.warehouse.Models.OrderedProduct;
import com.example.patryk.warehouse.Models.Pallet;
import com.example.patryk.warehouse.Models.Supply;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.REST.Rest;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplyPalletsFragment extends SupplyBaseFragment {

    private static String SUPPLY_CODE = "SUPPLY";
    private Supply supply;

    private RecyclerView recyclerView;
    private List<Pallet> pallets = new ArrayList<>();
    private PalletRecyclerViewAdapter adapter;

    private TextView typeOfSupply;
    private TextView arriveDate;
    private TextView factureNo;
    private TextView donePallet;

    private int countOfDonePallets = 0;

    public static SupplyPalletsFragment newInstance(Supply supply) {

        Bundle args = new Bundle();
        args.putSerializable(SUPPLY_CODE,supply);
        SupplyPalletsFragment fragment = new SupplyPalletsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supply_palletes_fragment,container,false);
        supply = (Supply) getArguments().getSerializable(SUPPLY_CODE);
        findViews(view);
        setAdapter();
        return view;
    }

    private void setLabels(){
        if(supply != null){
            if ((supply.getTypeOfSupply() != null)) {
                typeOfSupply.setText(supply.getTypeOfSupply());
            }

            if(supply.getBarCodeOfSupply() != null){
                factureNo.setText(supply.getBarCodeOfSupply());
            }

            if(supply.getArriveDate() != null){
                arriveDate.setText(supply.getArriveDate().substring(0,10));
            }

            countOfDonePallets = 0;

            for (Pallet pallet : pallets) {
                if(pallet.isDone()){
                    countOfDonePallets++;
                }
            }

            donePallet.setText(String.valueOf(countOfDonePallets)+"/"+String.valueOf(pallets.size()));
        }
    }

    private void findViews(View v){
        recyclerView = v.findViewById(R.id.spf_recyclerView);
        typeOfSupply = v.findViewById(R.id.spf_typeOfSupply);
        arriveDate = v.findViewById(R.id.spf_arriveDate);
        factureNo = v.findViewById(R.id.spf_factureNo);
        donePallet = v.findViewById(R.id.spf_donePallets);
    }

    private void setAdapter(){
        pallets = supply.getPaletts();
        adapter = new PalletRecyclerViewAdapter(pallets,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
        setListeners();
    }

    private void setListeners(){
        adapter.setOnClickListener(new PalletRecyclerViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if(!supply.getPaletts().get(position).isDone()) {
                    changeFragment(PalletProductsFragment.newInstance(supply,position),true);
                }else{
                    Toast.makeText(getContext(), "Ta paleta już jest zrobiona", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        checkIfPalletsIsDone();
        setLabels();


    }

    private void checkIfPalletsIsDone(){

        for (Pallet pallet : supply.getPaletts()) {
            boolean isDone = true;
            List<OrderedProduct> productsOnPalette = new ArrayList<>();
            productsOnPalette.addAll(pallet.getUsedProducts());
            for (OrderedProduct orderedProduct : productsOnPalette) {
                if(orderedProduct.getCount() != 0){
                    isDone = false;
                }
            }
            if(isDone){
                pallet.setDone(true);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
