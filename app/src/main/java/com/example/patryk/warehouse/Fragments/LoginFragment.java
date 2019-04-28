package com.example.patryk.warehouse.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.patryk.warehouse.BaseFragment.BaseFragment;
import com.example.patryk.warehouse.Components.FancyEditText;
import com.example.patryk.warehouse.Models.SignIn;
import com.example.patryk.warehouse.Models.User;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.REST.Rest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        userName.setText("admin");
        userPassword.setText("qwerty");
    }

    private void setOnClickListeners() {
        signIn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lf_sign_in:
                signIn(userName.getText().toString(), userPassword.getText().toString());
                break;
        }
    }

    private void signIn(String username, String password){
        SignIn user = new SignIn(username,password);
        Rest.getRest().login(user).enqueue(new Callback<SignIn>() {
            @Override
            public void onResponse(Call<SignIn> call, Response<SignIn> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Rest.token = "Bearer " +  response.body().getToken();
                    getNavigationsInteractions().changeFragment(MainFragment.newInstance(response.body().getUser()), false);
                }else{
                    Toast.makeText(getContext(), "Nie istnieje taki użytkownik", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignIn> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}
