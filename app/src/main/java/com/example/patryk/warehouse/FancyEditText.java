package com.example.patryk.warehouse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FancyEditText extends android.support.v7.widget.AppCompatEditText implements View.OnTouchListener {

    private static int TYPE_PASSWORD = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;

    private int width;
    private int height;
    private Paint backgroundPaint;
    private Paint iconBackgroundPaint;
    private Paint outlineBackgroundPaint;

    private int backgroundColor;
    private int color;
    private int iconColor;
    private int outlineColor;
    private Drawable icon;
    private Drawable rightIcon;

    private boolean passwordIsVisible = false;


    public FancyEditText(Context context) {
        super(context);
    }

    public FancyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FancyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        draw();
        super.onDraw(canvas);

    }

    private void draw() {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(bitmap);

        // outline
        mCanvas.drawRoundRect(0, 0, width, height, height / 2, height / 2, outlineBackgroundPaint);
        // background
        mCanvas.drawRoundRect(3, 3, width - 3, height - 3, height / 2, height / 2, backgroundPaint);
        // left circle
        mCanvas.drawOval(0, 0, height, height, iconBackgroundPaint);
        int padding = height / 4;

        // bar
        if (getText().toString().equals("") && !hasFocus()) {
            mCanvas.drawRect(height / 2, 0, width - (2 * height), height, iconBackgroundPaint);
            mCanvas.drawArc(width - (3 * height), -height, width - height, height, 0, 90, true, iconBackgroundPaint);
            setHintTextColor(iconColor);
        } else {
            setHintTextColor(color);
        }

        if (icon != null) {

            icon.setBounds(0 + padding, 0 + padding, height - padding, height - padding);
            icon.draw(mCanvas);
        }

        if (getText().length() > 0 && hasFocus()) {
            if (getInputType() == TYPE_PASSWORD) {
                rightIcon = getResources().getDrawable(R.drawable.ic_visibilityoff, null);
            } else if (passwordIsVisible == true) {
                rightIcon = getResources().getDrawable(R.drawable.ic_visibilityon, null);
            } else {
                rightIcon = getResources().getDrawable(R.drawable.ic_remove, null);
            }
            rightIcon.setTint(color);
            rightIcon.setBounds(width - height + padding, padding, width - padding, height - padding);
            rightIcon.draw(mCanvas);
        }

        Drawable back = new BitmapDrawable(getResources(), bitmap);
        setBackground(back);

        setPadding(height + padding, 0, height + padding, 0);
    }

    private void initPaint() {
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        backgroundPaint.setColor(backgroundColor);

        iconBackgroundPaint = new Paint();
        iconBackgroundPaint.setAntiAlias(true);
        iconBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        iconBackgroundPaint.setColor(color);

        outlineBackgroundPaint = new Paint();
        outlineBackgroundPaint.setAntiAlias(true);
        outlineBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        outlineBackgroundPaint.setColor(outlineColor);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.FancyEditText);
        int count = typedArray.getIndexCount();
        try {

            for (int i = 0; i < count; ++i) {

                int attr = typedArray.getIndex(i);
                // the attr corresponds to the title attribute
                if (attr == R.styleable.FancyEditText_backgroundColor) {
                    backgroundColor = typedArray.getColor(attr, getResources().getColor(R.color.colorPrimaryDark, null));
                } else if (attr == R.styleable.FancyEditText_color) {
                    color = typedArray.getColor(attr, getResources().getColor(R.color.colorAccent, null));
                } else if (attr == R.styleable.FancyEditText_iconLeft) {
                    //icon = getResources().getDrawable(typedArray.getResourceId(attr,0),null);
                    icon = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.FancyEditText_iconColor) {
                    iconColor = typedArray.getColor(attr, getResources().getColor(R.color.colorPrimary, null));
                }else if (attr == R.styleable.FancyEditText_outlineColor) {
                    outlineColor = typedArray.getColor(attr, getResources().getColor(R.color.colorPrimaryDark, null));
                }
            }
        }

        // the recycle() will be executed obligatorily
        finally {
            // for reuse
            typedArray.recycle();
        }
        icon.setTint(iconColor);
        setOnTouchListener(this);
        initPaint();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() >= width - height && hasFocus()) {
                    if (!passwordIsVisible) {
                        passwordIsVisible = true;
                        setTransformationMethod(null);
                        setSelection(getText().length());
                    } else if (passwordIsVisible) {
                        passwordIsVisible = false;
                        setTransformationMethod(new PasswordTransformationMethod());
                        setSelection(getText().length());
                    } else {
                        setText("");
                    }
                }
                break;
        }

        return false;
    }
}
