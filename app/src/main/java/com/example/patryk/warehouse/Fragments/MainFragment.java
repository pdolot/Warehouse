package com.example.patryk.warehouse.Fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.patryk.warehouse.Adapters.ViewPagerAdapter;
import com.example.patryk.warehouse.BaseFragment.BaseFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.ChangeLocation.ChangeLocationBaseFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Order.OrderBaseFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Search.SearchBaseFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.Supply.SupplyBaseFragment;
import com.example.patryk.warehouse.Fragments.ViewPagerFragments.UserInfo.UserInfoFragment;
import com.example.patryk.warehouse.Models.User;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.REST.Rest;
import com.example.patryk.warehouse.Components.mViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends BaseFragment implements ViewPager.OnPageChangeListener,View.OnClickListener {

    private static String USER = "USER";
    public static User user;
    private mViewPager viewPager;
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
    private TextView userName, userId, userWorkHours;

    private TextView orderCount;
    private ImageView countBackground;
    private String count = "0";

    public static MainFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable(USER,user);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getArguments().getSerializable(USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        findViews(view);
        setLabels();
        initFragments();
        initViewPager(0);
        setOnClickListeners();
        setElevation(0);
        getOrdersCount();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getOrdersCount();
                    }
                });
            }
        }, 0, 120000);
        return view;
    }

    private void getOrdersCount(){
        Rest.getRest().getOrdersCount(Rest.token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null){
                    count = response.body();
                    orderCount.setText(count);
                    if(count.equals("0")){
                        countBackground.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.possitive,null)));
                    }else{
                        countBackground.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryDark,null)));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
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
        orderCount = v.findViewById(R.id.mf_orderCount);
        countBackground = v.findViewById(R.id.mf_count_background);

        //user
        userName = v.findViewById(R.id.of_userName);
        userId = v.findViewById(R.id.of_userID);
        userWorkHours = v.findViewById(R.id.of_userWorkHours);

        options.add(searchProduct);
        options.add(orders);
        options.add(returns);
        options.add(delivery);
        options.add(dailyStats);
        options.add(logout);
    }

    private void setLabels(){
        userName.setText(user.getFirstName() + " " + user.getLastName());
        userId.setText("ID Pracownika: " + String.valueOf(user.getId()));
        userWorkHours.setText("Godziny pracy: " + user.getUserWorkHours());
    }

    private void initFragments(){
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragment(SearchBaseFragment.newInstance(),"Wyszukaj produkt");
        viewPagerAdapter.addFragment(OrderBaseFragment.newInstance(),"Zlecenia");
        viewPagerAdapter.addFragment(ChangeLocationBaseFragment.newInstance(),"Zmień lokalizację");
        viewPagerAdapter.addFragment(SupplyBaseFragment.newInstance(),"Zwroty / Dostawy");
        viewPagerAdapter.addFragment(UserInfoFragment.newInstance(),"Dziennik pracownika");
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
                getActivity().finish();
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
