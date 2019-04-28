package com.example.patryk.warehouse.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.patryk.warehouse.R;

public class ProgressArrow extends View {

    private int viewWidth;
    private int viewHeight;
    private int centerX;
    private int centerY;

    //
    private Paint progressPaint;
    private Paint backgroundPaint;
    private Paint thumbPaint;
    private Paint arrowPaint;
    private Paint arrowPaintFull;

    //
    private RectF backgroundDraw;
    private RectF progressDraw;
    private int startAngle = 0;
    private int sweepAngle = 0;

    //Thumb
    private int thumbXpos;
    private int thumbYpos;

    private int strokeWidth = 10;
    private int radius;

    private int progress = 0;
    private int angleMAX = 360;
    private int backgroundColor;
    private int progressFirstColor;
    private int progressSecondColor;


    private int rotation = 0;
    private int endXPos;
    private int endYPos;
    private int arrowLength = 30;

    public ProgressArrow(Context context) {
        super(context);
    }

    public ProgressArrow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ProgressArrow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public ProgressArrow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void initPaint(){

        //double c = Math.sqrt((radius*radius) + (strokeWidth*strokeWidth));
        //double needAngle = radius/c;

        //Paint
        progressPaint = new Paint();
        progressPaint.setColor(progressFirstColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(strokeWidth + 2.5f);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        arrowPaint = new Paint();
        arrowPaint.setColor(backgroundColor);
        arrowPaint.setAntiAlias(true);
        arrowPaint.setStyle(Paint.Style.FILL);

        arrowPaintFull = new Paint();
        arrowPaintFull.setColor(progressFirstColor);
        arrowPaintFull.setAntiAlias(true);
        arrowPaintFull.setStyle(Paint.Style.FILL);

        thumbPaint = new Paint();
        thumbPaint.setAntiAlias(true);

    }

    private void init(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.FancyEditText);
        int count = typedArray.getIndexCount();
        try{

            for (int i = 0; i < count; ++i) {

                int attr = typedArray.getIndex(i);
                // the attr corresponds to the title attribute
                if(attr == R.styleable.FancyEditText_angleMAX){
                    angleMAX = typedArray.getInt(attr,360);
                }else if(attr == R.styleable.FancyEditText_progress) {
                    progress = typedArray.getInt(attr,0);
                    if(progress > 100) progress = 100;
                    sweepAngle = (progress * angleMAX) / 100;
                }else if(attr == R.styleable.FancyEditText_progressStrokeWidth){
                    strokeWidth = typedArray.getInt(attr,10);
                } if(attr == R.styleable.FancyEditText_progressBackgroundColor){
                    backgroundColor = typedArray.getColor(attr,getResources().getColor(R.color.colorPrimaryDark,null));
                }else if(attr == R.styleable.FancyEditText_progressFirstColor){
                    progressFirstColor = typedArray.getColor(attr,getResources().getColor(R.color.colorPrimary,null));
                }else if(attr == R.styleable.FancyEditText_progressSecondColor){
                    progressSecondColor = typedArray.getColor(attr, getResources().getColor(R.color.colorAccent,null));
                }
            }
        }
        // the recycle() will be executed obligatorily
        finally {
            // for reuse
            typedArray.recycle();
        }

        measureRotation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(backgroundDraw,startAngle,angleMAX,false,backgroundPaint);
        canvas.drawArc(progressDraw,startAngle,sweepAngle,false,progressPaint);

        int r = strokeWidth / 2;



        if(progress == 100){
            canvas.rotate(-rotation,endXPos,endYPos);
            canvas.rotate(45,endXPos,endYPos);
            canvas.drawRoundRect(endXPos - strokeWidth,endYPos - strokeWidth ,endXPos + arrowLength,endYPos,r,r,arrowPaintFull);
            canvas.rotate(90,endXPos - r,endYPos - r);
            canvas.drawRoundRect(endXPos - strokeWidth,endYPos - strokeWidth ,endXPos + arrowLength,endYPos,r,r,arrowPaintFull);
        }else{
            canvas.rotate(-rotation,endXPos,endYPos);
            canvas.rotate(45,endXPos,endYPos);
            canvas.drawRoundRect(endXPos - strokeWidth,endYPos - strokeWidth ,endXPos + arrowLength,endYPos,r,r,arrowPaint);
            canvas.rotate(90,endXPos - r,endYPos - r);
            canvas.drawRoundRect(endXPos - strokeWidth,endYPos - strokeWidth ,endXPos + arrowLength,endYPos,r,r,arrowPaint);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        backgroundDraw = new RectF();
        progressDraw = new RectF();

        viewWidth = getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        viewHeight = getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);

        int min = Math.min(viewHeight,viewWidth);
        int padding = getPaddingLeft() + arrowLength;
        int stroke = strokeWidth / 2;
        radius = (min - padding) / 2 - stroke;

        centerX = viewWidth / 2;
        centerY = viewHeight / 2;

        backgroundDraw.set(centerX - radius,centerY -radius,centerX + radius,centerY + radius);
        progressDraw.set(centerX - radius,centerY - radius,centerX + radius,centerY + radius);

        thumbXpos = centerX + (int) (radius * Math.cos(Math.toRadians(startAngle + sweepAngle )));
        thumbYpos = centerY + (int) (radius * Math.sin(Math.toRadians(startAngle + sweepAngle )));

        endXPos = centerX + (int) (radius * Math.cos(Math.toRadians(angleMAX + rotation)));
        endYPos = centerY + (int) (radius * Math.sin(Math.toRadians(angleMAX + rotation)));

        initPaint();

    }

    public void setProgress(int progress){
        if(progress <= 100) {
            this.progress = progress;
            measureProgress(progress);
            thumbXpos = centerX + (int) (radius * Math.cos(Math.toRadians(startAngle + sweepAngle)));
            thumbYpos = centerY + (int) (radius * Math.sin(Math.toRadians(startAngle + sweepAngle)));
            invalidate();
        }
    }

    public int getProgress(){
        return progress;
    }

    private void measureProgress(int progress){
        sweepAngle = (progress * angleMAX) / 100;
    }

    private void measureRotation(){
        rotation = (180 - angleMAX) / 2;
        startAngle += rotation;
    }
}

