package com.example.patryk.warehouse.Fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.patryk.warehouse.Adapters.ViewPagerAdapter;
import com.example.patryk.warehouse.BaseFragment.BaseFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Order.OrderBaseFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Search.SearchBaseFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Search.SearchFragment;
import com.example.patryk.warehouse.R;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment implements ViewPager.OnPageChangeListener,View.OnClickListener {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TextView fragmentTitle;
    private RelativeLayout optionsButton;
    private DrawerLayout drawerLayout;
    private LinearLayout searchProduct;
    private LinearLayout orders;
    private LinearLayout returns;
    private LinearLayout delivery;
    private LinearLayout dailyStats;
    private LinearLayout logout;
    private List<LinearLayout> options = new ArrayList<>();

    public static MainFragment newInstance() {

        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        findViews(view);
        initFragments();
        initViewPager(0);
        setOnClickListeners();
        setElevation(0);
        return view;
    }

    private void findViews(View v){
        viewPager = v.findViewById(R.id.mf_viewPager);
        fragmentTitle = v.findViewById(R.id.mf_title);
        optionsButton = v.findViewById(R.id.mf_options);
        drawerLayout = v.findViewById(R.id.mf_drawerLayout);

        searchProduct = v.findViewById(R.id.of_searchProduct);
        orders = v.findViewById(R.id.of_orders);
        returns = v.findViewById(R.id.of_returns);
        delivery = v.findViewById(R.id.of_delivery);
        dailyStats = v.findViewById(R.id.of_dailyStats);
        logout = v.findViewById(R.id.of_logout);

        options.add(searchProduct);
        options.add(orders);
        options.add(returns);
        options.add(delivery);
        options.add(dailyStats);
        options.add(logout);
    }

    private void initFragments(){
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragment(SearchBaseFragment.newInstance(),"Wyszukaj produkt");
        viewPagerAdapter.addFragment(OrderBaseFragment.newInstance(),"Zamówienia");
        viewPagerAdapter.addFragment(SearchFragment.newInstance(),"Zwroty");
        viewPagerAdapter.addFragment(SearchFragment.newInstance(),"Dostawy");
        viewPagerAdapter.addFragment(SearchFragment.newInstance(),"Dziennik");
    }

    private void initViewPager(int startPosition){
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(startPosition);
        viewPager.addOnPageChangeListener(this);
        fragmentTitle.setText(viewPagerAdapter.getPageTitle(startPosition));
        viewPager.setOffscreenPageLimit(5);
    }

    private void setOnClickListeners(){
        optionsButton.setOnClickListener(this);
        searchProduct.setOnClickListener(this);
        orders.setOnClickListener(this);
        delivery.setOnClickListener(this);
        returns.setOnClickListener(this);
        dailyStats.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        fragmentTitle.setText(viewPagerAdapter.getPageTitle(i));
        setElevation(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mf_options:
                drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.of_searchProduct:
                setElevation(0);
                viewPager.setCurrentItem(0);
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.of_orders:
                setElevation(1);
                viewPager.setCurrentItem(1);
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.of_returns:
                setElevation(2);
                viewPager.setCurrentItem(2);
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.of_delivery:
                setElevation(3);
                viewPager.setCurrentItem(3);
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.of_dailyStats:
                setElevation(4);
                viewPager.setCurrentItem(4);
                drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.of_logout:
                drawerLayout.closeDrawer(Gravity.START);
                break;
        }
    }

    private void setElevation(int choosedItem){
        for(LinearLayout l : options){
            l.setElevation(0);
        }
        options.get(choosedItem).setElevation(4);
    }
}
