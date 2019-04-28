package com.example.patryk.warehouse.Fragments.ViewPagerFragments.Supply;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.patryk.warehouse.Fragments.Scanner;
import com.example.patryk.warehouse.Models.Id;
import com.example.patryk.warehouse.Models.Pallet;
import com.example.patryk.warehouse.Models.Supply;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.REST.Rest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplyFragment extends SupplyBaseFragment implements View.OnClickListener {

    private ImageView scanner;
    private EditText supplyBarCode;
    private LinearLayout takeSupply;

    public static String SUPPLY_FRAGMENT_RESULT_CODE = "";
    private Supply supply;
    private int counter = 0;

    public static SupplyFragment newInstance() {
        return new SupplyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supply_fragment, container, false);
        findViews(view);
        setListeners();
        return view;
    }

    private void findViews(View v){
        scanner = v.findViewById(R.id.sf_scannerButton);
        supplyBarCode = v.findViewById(R.id.sf_supplyBarCode);
        takeSupply = v.findViewById(R.id.sf_takeSupply_button);

        supplyBarCode.setText("1/2019/15/04");
    }

    private void setListeners(){
        scanner.setOnClickListener(this);
        takeSupply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sf_scannerButton:
                changeFragment(Scanner.newInstance("Supply"), true);
                break;
            case R.id.sf_takeSupply_button:
                SUPPLY_FRAGMENT_RESULT_CODE = supplyBarCode.getText().toString();
                if(!SUPPLY_FRAGMENT_RESULT_CODE.equals("")){
                    fetch_supply();
                }
                break;
        }
    }

    private void fetch_supply(){
        supply = new Supply();
        supply.setBarCodeOfSupply(SUPPLY_FRAGMENT_RESULT_CODE);
        SUPPLY_FRAGMENT_RESULT_CODE = "";
        Rest.getRest().getSupply(Rest.token,supply).enqueue(new Callback<Supply>() {
            @Override
            public void onResponse(Call<Supply> call, Response<Supply> response) {
                if(response.isSuccessful() && response.body() != null){
                    supply = response.body();
                    if(response.body().getId() != null){
                        for (int i = 0; i < supply.getPaletts().size(); i++) {
                            fetch_data(i);
                        }
                    }else{
                        Toast.makeText(getContext(), "Błędny numer faktury", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getContext(), "Błędny numer faktury", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Supply> call, Throwable t) {

            }
        });
    }

    private void fetch_data(final int position){
        Id id = new Id();
        id.setId(supply.getPaletts().get(position).getId());
        Rest.getRest().getProductFromPallet(Rest.token,id).enqueue(new Callback<Pallet>() {
            @Override
            public void onResponse(Call<Pallet> call, Response<Pallet> response) {
                if(response.isSuccessful() && response.body() != null){
                    counter++;
                    supply.getPaletts().get(position).setBarCode(response.body().getBarCode());
                    supply.getPaletts().get(position).setUsedProducts(response.body().getUsedProducts());
                    if(counter == supply.getPaletts().size()){
                        changeFragment(SupplyPalletsFragment.newInstance(supply),true);
                    }
                }
            }

            @Override
            public void onFailure(Call<Pallet> call, Throwable t) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        counter = 0;
        if(!SUPPLY_FRAGMENT_RESULT_CODE.equals("")){
            fetch_supply();
        }
    }
}
