package com.example.patryk.warehouse.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.patryk.warehouse.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Chart extends View {
    private int viewWidth;
    private int viewHeight;
    private int padding;
    private int bottomPadding;
    private int maxHeight;
    private int maxWidth;

    private int chartBarsCount;
    private List<Integer> chartBarsValues = new ArrayList<>();
    private List<Integer> chartIntervals = new ArrayList<>();
    private List<Integer> thumbPositions = new ArrayList<>();
    private List<String> dates = new ArrayList<>();
    private String[] days = {"ND", "PON", "WT", "SR", "CZW", "PT","SB"};

    private float[] radii = new float[8];

    //chartPosition
    private int top, left, bottom, right;

    private int silverMedium = getResources().getColor(R.color.silverDark, null);
    private int gray = getResources().getColor(R.color.grayLight, null);
    private int primary = getResources().getColor(R.color.possitive, null);
    private int primaryDark = getResources().getColor(R.color.primaryDark, null);
    private int textColor = getResources().getColor(R.color.text, null);
    private Paint background;
    private Paint line;
    private Paint bar;
    private Paint currentBar;
    private float thumbCenterX;
    private float thumbCenterY;
    private int thumbSize;
    private int thumbIndex;
    private String title = "";

    // bar
    private int barWidth;
    private int space;
    private Paint titlePaint;

    public Chart(Context context) {
        super(context);
        init();
    }

    public Chart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Chart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        for (int i = 0; i < 8; i++) {
            radii[i] = 0;
        }
    }

    public void setChartBarsValues(List<Integer> chartBarsValues) {
        this.chartBarsValues = chartBarsValues;
        this.chartBarsCount = chartBarsValues.size();
        setValues();
        invalidate();
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    private String getDayOfWeek(String date) {
        Calendar c = Calendar.getInstance();
        int day = Integer.valueOf(date.substring(0, 2));
        int month = Integer.valueOf(date.substring(3, 5)) - 1;
        int year = Integer.valueOf(date.substring(6, 10));
        c.set(year, month, day);
        int d = c.get(Calendar.DAY_OF_WEEK) - 1;
        return days[d];
    }

    public int getCurrentValue() {
        return chartBarsValues.get(thumbIndex);
    }

    private void initPaint() {
        background = new Paint();
        background.setAntiAlias(true);
        background.setStyle(Paint.Style.FILL);
        background.setColor(silverMedium);

        line = new Paint();
        line.setAntiAlias(true);
        line.setStyle(Paint.Style.FILL);
        line.setColor(gray);
        line.setTextSize(16);

        bar = new Paint();
        bar.setAntiAlias(true);
        bar.setStyle(Paint.Style.FILL);
        bar.setColor(primaryDark);
        bar.setTextSize(20);

        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setStyle(Paint.Style.FILL);
        titlePaint.setColor(textColor);
        titlePaint.setTextSize(36);

        currentBar = new Paint();
        currentBar.setAntiAlias(true);
        currentBar.setStyle(Paint.Style.FILL);
        currentBar.setColor(primary);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(0, 0, viewWidth, viewHeight - bottomPadding, 23, 23, background);

        int titleWidth = (int) titlePaint.measureText(title);
        canvas.drawText(title,viewWidth/2 - titleWidth/2, top/2,titlePaint);

        // horizontal lines
        if(chartBarsCount > 0){
            int max = Collections.max(chartBarsValues);

            int linesCount = 5;
            int lineSpace = maxHeight / linesCount;

            for (int i = 0; i < linesCount + 1; i++) {
                canvas.drawRect(left, viewHeight - (lineSpace * i) - bottom - 3, viewWidth - right, viewHeight - (lineSpace * i) - bottom, line);
                String value = String.format("%.1f", i * ((double) max / linesCount));
                int textWidth = (int) bar.measureText(value);
                canvas.drawText(value, (padding / 2) - (textWidth / 2), viewHeight - (lineSpace * i) - bottom, bar);
            }

            for (int i = 0; i < chartBarsCount; i++) {
                int startPos = padding + (space + (i * (2 * barWidth)));
                int barHeight = maxHeight;

                if (chartBarsValues.get(i) != 0) {
                    int barValue = chartBarsValues.get(i) * 100 / max;
                    barHeight = maxHeight * (100 - barValue) / 100;
                }

                GradientDrawable chartBar = new GradientDrawable();
                chartBar.setBounds(startPos, top + barHeight, startPos + barWidth, viewHeight - bottom);
                chartBar.setCornerRadii(radii);

                if (i == thumbIndex) {
                    //canvas.drawRoundRect(startPos, top + barHeight, startPos + barWidth, viewHeight - bottom, 10, 10, currentBar);
                    chartBar.setColor(primary);
                } else {
                    //canvas.drawRoundRect(startPos, top + barHeight, startPos + barWidth, viewHeight - bottom, 10, 10, bar);
                    chartBar.setColor(primaryDark);
                }
                chartBar.draw(canvas);

                String day = getDayOfWeek(dates.get(i));
                int textWidth = (int) bar.measureText(day) / 2;
                canvas.drawText(day, thumbPositions.get(i) - textWidth, viewHeight - bottom + 26, bar);
            }
        }

        //draw circle
        Drawable drawable = getResources().getDrawable(R.drawable.ic_thumb, null);
        drawable.setBounds((int) thumbCenterX - thumbSize, (int) thumbCenterY - thumbSize, (int) thumbCenterX + thumbSize, (int) thumbCenterY + thumbSize);
        drawable.draw(canvas);

    }

    private void setValues() {
        bottomPadding = viewHeight / 16;
        padding = viewHeight / 6;

        left = right = top = padding;
        bottom = padding + bottomPadding;
        maxHeight = (viewHeight - bottomPadding) - (padding * 2);
        maxWidth = viewWidth - (padding * 2);

        barWidth = maxWidth / (chartBarsCount * 2);
        space = barWidth / 2;


        thumbIndex = chartBarsCount - 1;

        chartIntervals.clear();
        chartIntervals.add(0);
        thumbPositions.clear();
        for (int i = 0; i < chartBarsCount; i++) {
            chartIntervals.add(padding + ((barWidth * 2) * (1 + i)));
            thumbPositions.add(padding + (space + (i * (2 * barWidth))) + space);
        }

        thumbSize = bottomPadding * 2;
        thumbCenterY = viewHeight - bottomPadding;
        thumbCenterX = thumbPositions.get(thumbPositions.size() - 1);

        for (int i = 0; i < 4; i++) {
            radii[i] = space;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initPaint();
        viewWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        viewHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        if(chartBarsCount!=0){
            setValues();
        }
    }

    public int getThumbIndex(){
        return thumbIndex;
    }

    private void setCurrentBar(float pos) {
        for (int i = 0; i < chartIntervals.size() - 1; i++) {
            if (chartIntervals.get(i) <= pos && chartIntervals.get(i + 1) >= pos) {
                thumbIndex = i;

            }
        }
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public float getThumbCenterX() {
        return thumbCenterX;
    }

    public void setThumbCenterX(float thumbCenterX) {
        this.thumbCenterX = thumbCenterX;
        setCurrentBar(thumbCenterX);
        invalidate();
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setThumbPosition() {
        thumbCenterX = thumbPositions.get(thumbIndex);
        invalidate();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
