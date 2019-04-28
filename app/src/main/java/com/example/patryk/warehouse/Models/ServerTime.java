package com.example.patryk.warehouse.Models;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ServerTime {
    @SerializedName("time")
    private String time;
    private Calendar calendar = Calendar.getInstance();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Calendar getCalendar() throws ParseException {
        Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(time);
        calendar.setTime(date);
        return calendar;
    }

}
