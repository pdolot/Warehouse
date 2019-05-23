package com.example.patryk.warehouse.Fragments.ViewPagerFragments.UserInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patryk.warehouse.Components.Chart;
import com.example.patryk.warehouse.Components.HorizontalProgressBar;
import com.example.patryk.warehouse.Components.mScrollView;
import com.example.patryk.warehouse.Components.mViewPager;
import com.example.patryk.warehouse.Fragments.MainFragment;
import com.example.patryk.warehouse.Models.ServerTime;
import com.example.patryk.warehouse.Models.UserWorkDay;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.REST.Rest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {

    private static int PERIOD_TIME = 60000;
    private Chart chart;
    private mScrollView scrollView;
    private mViewPager viewPager;
    private List<Integer> ordersCountList = new ArrayList<>();
    private List<UserWorkDay> workDays = new ArrayList<>();
    private List<String> dates = new ArrayList<>();
    private TextView userName, workHour, userId, date, ordersCount, performance, positions, pallets;
    private Calendar calendar = Calendar.getInstance();
    private HorizontalProgressBar timeLeft;
    private ImageView refresh;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_info_fragment, container, false);
        findViews(view);
        setListeners();
        fetch_data();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (setTimeLeft()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + (PERIOD_TIME/1000));
                        }
                    });
                }
            }
        }, 0, PERIOD_TIME);
        return view;
    }

    private void findViews(View v) {
        chart = v.findViewById(R.id.uif_chart);
        viewPager = getActivity().findViewById(R.id.mf_viewPager);
        date = v.findViewById(R.id.uif_date);
        ordersCount = v.findViewById(R.id.uif_ordersCount);
        performance = v.findViewById(R.id.uif_performance);
        positions = v.findViewById(R.id.uif_positions);
        pallets = v.findViewById(R.id.uif_pallets);
        timeLeft = v.findViewById(R.id.uif_timeLeft);
        scrollView = v.findViewById(R.id.uif_scrollView);
        userName = v.findViewById(R.id.uif_userName);
        userId = v.findViewById(R.id.uif_userID);
        workHour = v.findViewById(R.id.uif_userWorkHours);
        refresh = v.findViewById(R.id.uif_refresh);
    }

    private void setLabels(UserWorkDay day) {
        userName.setText(MainFragment.user.getFirstName() + " " + MainFragment.user.getLastName());
        userId.setText(String.valueOf(MainFragment.user.getId()));
        workHour.setText(MainFragment.user.getUserWorkHours());
        date.setText(day.getDay().getDate().replace('.', '/'));
        ordersCount.setText(String.valueOf(day.getDay().getAmountOfOrders()));
        performance.setText(String.valueOf(String.format("%.2f", day.getPerformance()).replace(',', '.')));
        positions.setText(String.valueOf(day.getDay().getAmountOfPositions()));
        pallets.setText(String.valueOf(String.format("%.2f", day.getDay().getPalettes()).replace(',', '.')));

        chart.setTitle("Liczba zleceń");
    }

    private void setListeners() {
        chart.setOnTouchListener(this);
        date.setOnClickListener(this);
        refresh.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.uif_chart:
                viewPager.setPagingEnabled(false);
                scrollView.setScrollingEnabled(false);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (event.getX() > chart.getPadding()
                                && event.getX() < chart.getViewWidth() - chart.getPadding()
                                && event.getY() > chart.getViewHeight() - chart.getPadding()) {
                            float x = event.getX() - chart.getThumbCenterX();
                            chart.setThumbCenterX(chart.getThumbCenterX() + x);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        chart.setThumbPosition();
                        setLabels(workDays.get(chart.getThumbIndex()));
                        viewPager.setPagingEnabled(true);
                        scrollView.setScrollingEnabled(true);
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.setPagingEnabled(true);
        getServerTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uif_refresh:
                fetch_data();
                break;

        }
    }

    private void fetch_data() {
        Rest.getRest().getUserWorkDays(Rest.token).enqueue(new Callback<List<UserWorkDay>>() {
            @Override
            public void onResponse(Call<List<UserWorkDay>> call, Response<List<UserWorkDay>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    workDays.clear();
                    workDays.addAll(response.body());
                    Collections.reverse(workDays);
                    ordersCountList.clear();
                    dates.clear();
                    for (UserWorkDay workDay : workDays) {
                        ordersCountList.add(workDay.getDay().getAmountOfOrders());
                        dates.add(workDay.getDay().getDate());
                    }
                    chart.setDates(dates);
                    chart.setChartBarsValues(ordersCountList);
                    setLabels(workDays.get(workDays.size() - 1));
                }
            }

            @Override
            public void onFailure(Call<List<UserWorkDay>> call, Throwable t) {

            }
        });
    }

    private void getServerTime() {
        Rest.getRest().getServerTime(Rest.token).enqueue(new Callback<ServerTime>() {
            @Override
            public void onResponse(Call<ServerTime> call, Response<ServerTime> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ServerTime serverTime = response.body();

                    try {
                        calendar = serverTime.getCalendar();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerTime> call, Throwable t) {

            }
        });
    }

    private boolean setTimeLeft() {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(calendar.getTime());
        calendar1.set(Calendar.HOUR_OF_DAY, MainFragment.user.getStartWorkHour());
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);

        calendar2.setTime(calendar.getTime());
        if(MainFragment.user.getEndWorkHour() < MainFragment.user.getStartWorkHour()){
            calendar2.set(Calendar.DAY_OF_YEAR,calendar2.get(Calendar.DAY_OF_YEAR)+1);
        }
        calendar2.set(Calendar.HOUR_OF_DAY, MainFragment.user.getEndWorkHour());
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);

        long hours = (calendar2.getTimeInMillis() - calendar1.getTimeInMillis()) / PERIOD_TIME;
        double period = (double) hours / 100;

        if (calendar.getTime().compareTo(calendar1.getTime()) < 0) {
            timeLeft.setProgress(0);
        } else if (calendar.getTime().compareTo(calendar2.getTime()) > 0) {
            timeLeft.setProgress(100);
        }

        if (calendar.getTime().compareTo(calendar1.getTime()) >= 0 && calendar.getTime().compareTo(calendar2.getTime()) <= 0) {
            long time = (calendar.getTimeInMillis() - calendar1.getTimeInMillis()) / PERIOD_TIME;
            int progress = (int) (time / period);
            timeLeft.setProgress(progress);
            return true;
        }

        return false;
    }
}
