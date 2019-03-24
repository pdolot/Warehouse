package com.example.patryk.warehouse.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.patryk.warehouse.BaseFragment.BaseFragment;
import com.example.patryk.warehouse.FancyEditText;
import com.example.patryk.warehouse.R;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout signIn;
    private FancyEditText userName;
    private FancyEditText userPassword;
    private ImageView imageView;

    private float start;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        findViews(view);
        setOnClickListeners();
        return view;
    }

    private void findViews(View v) {
        signIn = v.findViewById(R.id.lf_sign_in);
        userName = v.findViewById(R.id.lf_userName);
        userPassword = v.findViewById(R.id.lf_password);
        imageView = v.findViewById(R.id.imageview);
    }

    private void setOnClickListeners() {
        signIn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lf_sign_in:
                getNavigationsInteractions().changeFragment(MainFragment.newInstance(), false);
                break;
        }
    }
}
