package com.example.patryk.warehouse.Fragments.ViewPagerFragments.ChangeLocation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patryk.warehouse.Navigation.FragmentNavigation;
import com.example.patryk.warehouse.R;


public class ChangeLocationBaseFragment extends Fragment implements FragmentNavigation {

    public static ChangeLocationBaseFragment newInstance() {
        return new ChangeLocationBaseFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.change_location_base_fragment,container,false);
        changeFragment(ChangeLocationFragment.newInstance(),false);
        return view;
    }


    @Override
    public void changeFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.change_location_root,fragment);

        if(addToBackStack){
            fragmentTransaction.addToBackStack(fragment.toString());
        }
        fragmentTransaction.commit();
    }
}