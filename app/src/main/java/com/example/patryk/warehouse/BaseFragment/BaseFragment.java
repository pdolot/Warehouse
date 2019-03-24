package com.example.patryk.warehouse.BaseFragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.patryk.warehouse.Navigation.FragmentNavigation;

public class BaseFragment extends Fragment {

    FragmentNavigation navigationListener;

    public FragmentNavigation getNavigationsInteractions() {
        return navigationListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FragmentNavigation){
            navigationListener = (FragmentNavigation) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationListener = null;
    }

}
