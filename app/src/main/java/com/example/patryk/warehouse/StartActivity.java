package com.example.patryk.warehouse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.patryk.warehouse.Fragments.LoginFragment;
import com.example.patryk.warehouse.Navigation.FragmentNavigation;
import com.example.patryk.warehouse.REST.Rest;

public class StartActivity extends AppCompatActivity implements FragmentNavigation {

    // Start

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Rest.init();
        changeFragment(LoginFragment.newInstance(),false);
    }

    @Override
    public void changeFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.root,fragment);

        if(addToBackStack){
            fragmentTransaction.addToBackStack(fragment.toString());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Zamknij aplikację");
            builder.setMessage("Czy na pewno chcesz zamknąć aplikację?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    StartActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }else{
            StartActivity.super.onBackPressed();
        }
    }
}
