package com.example.patryk.warehouse.Components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class mScrollView extends ScrollView {
    private boolean scroll = true;

    public mScrollView(@NonNull Context context) {
        super(context);
    }

    public mScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.scroll) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.scroll) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setScrollingEnabled(boolean enabled) {
        this.scroll = enabled;
    }


}
