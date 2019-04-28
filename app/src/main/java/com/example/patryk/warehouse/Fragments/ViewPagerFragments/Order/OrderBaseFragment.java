package com.example.patryk.warehouse.Fragments.ViewPagerFragments.Order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.patryk.warehouse.Models.Id;
import com.example.patryk.warehouse.Models.Order;
import com.example.patryk.warehouse.Navigation.FragmentNavigation;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.REST.Rest;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderBaseFragment extends Fragment implements FragmentNavigation {

    public static OrderBaseFragment newInstance() {
        return new OrderBaseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.order_basefragment,container,false);
        getCurrentOrder();
        return view;
    }

    private void getCurrentOrder(){
        Rest.getRest().getCurrentOrder(Rest.token).enqueue(new Callback<Id>() {
            @Override
            public void onResponse(Call<Id> call, Response<Id> response) {
                //Toast.makeText(getContext(), String.valueOf(new GsonBuilder().setPrettyPrinting().create().toJson(response.body())), Toast.LENGTH_SHORT).show();
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getId()!=null){
                        fetch_order(response.body().getId());
                        //changeFragment(OrderFragment.newInstance(),false);
                    }
                }else{
                    changeFragment(OrdersFragment.newInstance(),false);

                }
            }

            @Override
            public void onFailure(Call<Id> call, Throwable t) {
                System.out.println(t.getMessage());
                changeFragment(OrdersFragment.newInstance(),false);
            }
        });
    }

    private void fetch_order(final Long order_id) {
        Id id = new Id();
        id.setId(order_id);
        Rest.getRest().getOrder(Rest.token, id).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response != null) {
                    Order order = response.body();
                    changeFragment(OrderFragment.newInstance(order),false);
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }

    @Override
    public void changeFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.orders_root,fragment);
        if(addToBackStack){
            fragmentTransaction.addToBackStack(fragment.toString());
        }
        fragmentTransaction.commit();
    }

}
