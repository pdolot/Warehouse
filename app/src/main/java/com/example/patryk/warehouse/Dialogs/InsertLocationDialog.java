package com.example.patryk.warehouse.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.patryk.warehouse.Adapters.TakeProductRecyclerViewAdapter;
import com.example.patryk.warehouse.Models.ProductToTake;
import com.example.patryk.warehouse.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InsertLocationDialog extends Dialog {

    public void setOnClickListener(View.OnClickListener onClickListener){
        accept.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);
    }

    private String location = "";

    private LinearLayout accept;
    private ImageView cancel;
    private EditText insertLocation;


    public InsertLocationDialog(@NotNull Context context) {
        super(context);
    }

    public String getLocation(){
        return insertLocation.getText().toString();
    }
    public void setLocation(String location){
        this.location = location;
        insertLocation.setText(location);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.insert_location_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        findViews();
    }

    private void findViews(){
        accept = findViewById(R.id.ild_accept);
        cancel = findViewById(R.id.ild_cancel);
        insertLocation = findViewById(R.id.ild_location);
    }

}
