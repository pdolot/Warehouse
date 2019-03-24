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

import com.example.patryk.warehouse.Navigation.FragmentNavigation;
import com.example.patryk.warehouse.R;

public class OrderBaseFragment extends Fragment implements FragmentNavigation {

    public static OrderBaseFragment newInstance() {
        return new OrderBaseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.order_basefragment,container,false);
        changeFragment(OrdersFragment.newInstance(),false);
        return view;
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
