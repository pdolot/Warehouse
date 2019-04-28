package com.example.patryk.warehouse.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.patryk.warehouse.R;

public class HorizontalProgressBar extends View {
    private int viewWidth;
    private int viewHeight;

    private int silverMedium = getResources().getColor(R.color.silverDark, null);
    private int primary = getResources().getColor(R.color.possitive, null);
    private int primaryDark = getResources().getColor(R.color.primaryDark, null);
    private Paint background;
    private Paint fill;
    private Paint fillFull;
    private int progress;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public HorizontalProgressBar(Context context) {
        super(context);
        initPaint();
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        viewHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
    }

    private void initPaint(){
        background = new Paint();
        background.setColor(silverMedium);
        background.setAntiAlias(true);
        background.setStyle(Paint.Style.FILL);

        fill = new Paint();
        fill.setColor(primaryDark);
        fill.setAntiAlias(true);
        fill.setStyle(Paint.Style.FILL);

        fillFull = new Paint();
        fillFull.setColor(primary);
        fillFull.setAntiAlias(true);
        fillFull.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(0,0,viewWidth,viewHeight,viewHeight/2,viewHeight/2,background);

        int width = progress * viewWidth / 100;
        if(progress == 100){
            canvas.drawRoundRect(0,0,width,viewHeight,viewHeight/2,viewHeight/2,fillFull);
        }else{
            canvas.drawRoundRect(0,0,width,viewHeight,viewHeight/2,viewHeight/2,fill);
        }

    }
}
