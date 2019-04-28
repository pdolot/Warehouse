package com.example.patryk.warehouse.Fragments.ViewPagerFragments.Supply;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Search.SearchFragment;
import com.example.patryk.warehouse.Navigation.FragmentNavigation;
import com.example.patryk.warehouse.R;

public class SupplyBaseFragment extends Fragment implements FragmentNavigation {

    public static SupplyBaseFragment newInstance() {
        return new SupplyBaseFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.supply_base_fragment,container,false);
        changeFragment(SupplyFragment.newInstance(),false);
        return view;
    }


    @Override
    public void changeFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.supply_root_frame,fragment);

        if(addToBackStack){
            fragmentTransaction.addToBackStack(fragment.toString());
        }
        fragmentTransaction.commit();
    }
}
