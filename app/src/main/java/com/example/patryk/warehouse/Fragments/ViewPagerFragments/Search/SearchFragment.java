package com.example.patryk.warehouse.Fragments.ViewPagerFragments.Search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.patryk.warehouse.Fragments.MainFragment;
import com.example.patryk.warehouse.Fragments.Scanner;
import com.example.patryk.warehouse.R;

public class SearchFragment extends SearchBaseFragment {

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
    public static String SEARCH_RESULT_CODE = "";
    private Button scan;
    private TextView textView;
    private EditText edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.search_fragment,container,false);
        scan = view.findViewById(R.id.scan);
        textView = view.findViewById(R.id.code);
        textView.setText(SEARCH_RESULT_CODE);

        edit = view.findViewById(R.id.edit_tmp);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(Scanner.newInstance("Search"),true);
            }
        });
        return view;
    }


}
