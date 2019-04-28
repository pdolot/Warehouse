package com.example.patryk.warehouse.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patryk.warehouse.Fragments.ViewPagerFragments.ChangeLocation.ChangeLocationFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Order.OrderFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Search.SearchFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Supply.PalletProductsFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Supply.SupplyFragment;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerSpreading extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private static String PRODUCT_ID;
    private int product_id;

    public static ScannerSpreading newInstance(int product_id) {
        Bundle args = new Bundle();
        args.putInt(PRODUCT_ID,product_id);
        ScannerSpreading fragment = new ScannerSpreading();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        product_id = getArguments().getInt(PRODUCT_ID);
        mScannerView = new ZXingScannerView(getActivity());
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
        PalletProductsFragment.PRODUCT_RESULT_CODE = rawResult.getText();
        PalletProductsFragment.PRODUCT_ID = product_id;
        getFragmentManager().popBackStack();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
