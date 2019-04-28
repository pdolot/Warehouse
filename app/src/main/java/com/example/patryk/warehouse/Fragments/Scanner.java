package com.example.patryk.warehouse.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.patryk.warehouse.Fragments.ViewPagerFragments.ChangeLocation.ChangeLocationFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Order.OrderFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Search.SearchFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Supply.SupplyFragment;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scanner extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private static String PARENT_FRAGMENT;
    private String parentFragment;

    public static Scanner newInstance(String parentFragment) {
        Bundle args = new Bundle();
        args.putString(PARENT_FRAGMENT,parentFragment);
        Scanner fragment = new Scanner();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        parentFragment = getArguments().getString(PARENT_FRAGMENT);
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if(parentFragment.equals("Search")){
            SearchFragment.SEARCH_RESULT_CODE = rawResult.getText();
        }else if(parentFragment.equals("Order")){
            OrderFragment.ORDER_RESULT_CODE = rawResult.getText();
        }else if(parentFragment.equals("ChangeLocation")){
            if(ChangeLocationFragment.CHANGE_LOCATION_RESULT_CODE.equals("")){
                ChangeLocationFragment.CHANGE_LOCATION_RESULT_CODE = rawResult.getText();
            }else{
                ChangeLocationFragment.CHANGE_LOCATION_RESULT_CODE2 = rawResult.getText();
            }
        }else if(parentFragment.equals("Supply")){
            SupplyFragment.SUPPLY_FRAGMENT_RESULT_CODE = rawResult.getText();
        }
        getFragmentManager().popBackStack();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
