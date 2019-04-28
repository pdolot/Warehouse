package com.example.patryk.warehouse.Fragments.ViewPagerFragments.ChangeLocation;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patryk.warehouse.Fragments.Scanner;
import com.example.patryk.warehouse.Models.Location;
import com.example.patryk.warehouse.Models.LocationsToChange;
import com.example.patryk.warehouse.Models.Product;
import com.example.patryk.warehouse.Models.ProductToTake;
import com.example.patryk.warehouse.Models.SerializedProduct;
import com.example.patryk.warehouse.Components.ProgressArrow;
import com.example.patryk.warehouse.Models.Status;
import com.example.patryk.warehouse.R;
import com.example.patryk.warehouse.REST.Rest;
import com.example.patryk.warehouse.Dialogs.TakeProductDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;

public class ChangeLocationFragment extends ChangeLocationBaseFragment implements View.OnClickListener {

    private LinearLayout input_layout, open_input;
    private ImageView barCode_button, open_icon;
    private EditText inputCode;

    private TextView first_location, second_location, step, tip;
    private ImageView firstDot, secondDot, thirdDot;
    private LinearLayout changePallets;

    private ProgressArrow progressArrowFirst, progressArrowSecond;

    public static String CHANGE_LOCATION_RESULT_CODE = "";
    public static String CHANGE_LOCATION_RESULT_CODE2 = "";

    private boolean inputIsOpen = false;
    private boolean isReturned = false;

    private Location location;
    private List<SerializedProduct> tookProducts = new ArrayList<>();

    Thread thread;

    public static ChangeLocationFragment newInstance() {
        return new ChangeLocationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_location_fragment, container, false);
        findViews(view);
        setListeners();
        return view;
    }

    private void setListeners() {
        barCode_button.setOnClickListener(this);
        open_input.setOnClickListener(this);
        first_location.setOnClickListener(this);
        changePallets.setOnClickListener(this);

        firstDot.setOnClickListener(this);
        secondDot.setOnClickListener(this);
        thirdDot.setOnClickListener(this);
    }

    private void findViews(View v) {
        barCode_button = v.findViewById(R.id.cl_bar_code);
        input_layout = v.findViewById(R.id.cl_input_layout);
        open_input = v.findViewById(R.id.cl_open_input);
        open_icon = v.findViewById(R.id.cl_open_icon);
        inputCode = v.findViewById(R.id.cl_inputCode);

        first_location = v.findViewById(R.id.cl_first_location);
        second_location = v.findViewById(R.id.cl_second_location);
        step = v.findViewById(R.id.cl_step);
        tip = v.findViewById(R.id.cl_tip);
        changePallets = v.findViewById(R.id.cl_changePallets);
        progressArrowFirst = v.findViewById(R.id.first_bar);
        progressArrowSecond = v.findViewById(R.id.second_bar);

        firstDot = v.findViewById(R.id.cl_firstDot);
        secondDot = v.findViewById(R.id.cl_secondDot);
        thirdDot = v.findViewById(R.id.cl_thirdDot);

        secondDot.setEnabled(false);
        thirdDot.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_open_input:
                if (inputIsOpen) {
                    closeInput();
                } else {
                    openInput();
                }
                break;
            // scanner button
            case R.id.cl_bar_code:
                if (inputIsOpen) {
                    if (CHANGE_LOCATION_RESULT_CODE.equals("")
                            && CHANGE_LOCATION_RESULT_CODE2.equals("")) {
                        CHANGE_LOCATION_RESULT_CODE = inputCode.getText().toString();
                        getInfoAboutLocation();
                    } else if (!CHANGE_LOCATION_RESULT_CODE.equals("")
                            && CHANGE_LOCATION_RESULT_CODE2.equals("")) {
                        CHANGE_LOCATION_RESULT_CODE2 = inputCode.getText().toString();
                        if(!CHANGE_LOCATION_RESULT_CODE2.equals(CHANGE_LOCATION_RESULT_CODE)){
                            setSecond_location();
                        }else {
                            CHANGE_LOCATION_RESULT_CODE2 = "";
                            Toast.makeText(getContext(), "Nie możesz odłożyć w to samo miejsce", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    changeFragment(Scanner.newInstance("ChangeLocation"), true);

                }
                break;
            case R.id.cl_first_location:
                progressArrowFirst.setProgress(progressArrowFirst.getProgress() + 2);
                break;
            case R.id.cl_changePallets:
                Toast.makeText(getContext(), "Zamieniam palety", Toast.LENGTH_SHORT).show();
                changeLocation();
                reset();
                break;
            case R.id.cl_firstDot:
                reset();
                break;
            case R.id.cl_secondDot:
                System.out.println("2nd clicked");
                isReturned = true;
                progressArrowSecond.setProgress(0);
                CHANGE_LOCATION_RESULT_CODE2 = "";
                second_location.setText("");
                step.setText("Krok 2");
                tip.setText(getResources().getString(R.string.cl_stepTwo));
                setDots(2, false);
                changePallets.setVisibility(View.INVISIBLE);
                barCode_button.setEnabled(true);
                getInfoAboutLocation();
                break;
            case R.id.cl_thirdDot:
                break;
        }
    }


    private void openInput() {
        input_layout.setVisibility(View.VISIBLE);
        open_icon.setImageResource(R.drawable.ic_arrowleft);
        barCode_button.setBackgroundResource(R.drawable.ic_done);
        inputIsOpen = true;
    }

    private void closeInput() {
        input_layout.setVisibility(View.GONE);
        open_icon.setImageResource(R.drawable.ic_arrowright);
        barCode_button.setBackgroundResource(R.drawable.ic_barcode_background);
        inputIsOpen = false;
    }

    private void setFirst_location() {
        isReturned = false;
        first_location.setText("LOK: " + CHANGE_LOCATION_RESULT_CODE);
        animateBar(progressArrowFirst);

        step.setText("Krok 2");
        tip.setText(getResources().getString(R.string.cl_stepTwo));
        setDots(2, true);
    }

    private void setSecond_location() {
        isReturned = false;
        first_location.setText("LOK: " + CHANGE_LOCATION_RESULT_CODE);
        progressArrowFirst.setProgress(100);

        second_location.setText("LOK: " + CHANGE_LOCATION_RESULT_CODE2);
        animateBar(progressArrowSecond);
        step.setText("Krok 3");
        tip.setText(getResources().getString(R.string.cl_stepThree));
        changePallets.setVisibility(View.VISIBLE);
        setDots(3, true);

        barCode_button.setEnabled(false);
    }


    private void setDots(int i, boolean clickable) {
        if (i == 2) {
            if (clickable) {
                secondDot.setScaleX(0.25f);
                secondDot.setScaleY(0.25f);
                secondDot.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary, null)));
            } else {
                thirdDot.setEnabled(false);
                thirdDot.setScaleX(0.125f);
                thirdDot.setScaleY(0.125f);
                thirdDot.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grayLight, null)));
            }
        } else if (i == 3) {
            if (clickable) {
                secondDot.setScaleX(0.25f);
                secondDot.setScaleY(0.25f);
                secondDot.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary, null)));
                secondDot.setEnabled(true);
                thirdDot.setScaleX(0.25f);
                thirdDot.setScaleY(0.25f);
                thirdDot.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary, null)));
            }
        } else {
            secondDot.setEnabled(false);
            thirdDot.setEnabled(false);

            secondDot.setScaleX(0.125f);
            secondDot.setScaleY(0.125f);
            secondDot.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grayLight, null)));

            thirdDot.setScaleX(0.125f);
            thirdDot.setScaleY(0.125f);
            thirdDot.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grayLight, null)));
        }
    }

    private void reset() {
        isReturned = true;
        progressArrowFirst.setProgress(0);
        progressArrowSecond.setProgress(0);
        CHANGE_LOCATION_RESULT_CODE = CHANGE_LOCATION_RESULT_CODE2 = "";
        first_location.setText("");
        second_location.setText("");
        step.setText("Krok 1");
        tip.setText(getResources().getString(R.string.cl_stepOne));
        setDots(1, false);

        changePallets.setVisibility(View.INVISIBLE);
        barCode_button.setEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!CHANGE_LOCATION_RESULT_CODE.equals("") && CHANGE_LOCATION_RESULT_CODE2.equals(""))
//            getInfoAboutLocation();
//        if (!CHANGE_LOCATION_RESULT_CODE2.equals("")) setSecond_location();
        if (!CHANGE_LOCATION_RESULT_CODE.equals("")
                && CHANGE_LOCATION_RESULT_CODE2.equals("")) {
            getInfoAboutLocation();
        }
        if (!CHANGE_LOCATION_RESULT_CODE2.equals("")) {
            if(!CHANGE_LOCATION_RESULT_CODE2.equals(CHANGE_LOCATION_RESULT_CODE)){
                setSecond_location();
            }else {
                CHANGE_LOCATION_RESULT_CODE2 = "";
                isReturned = true;
                setFirst_location();
                Toast.makeText(getContext(), "Nie możesz odłożyć w to samo miejsce", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void animateBar(final ProgressArrow progressArrow) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                progressArrow.setProgress(0);
                while (progressArrow.getProgress() <= 100 && !isReturned) {
                    progressArrow.setProgress(progressArrow.getProgress() + 2);
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (!isReturned) {
            thread.start();
        }

    }

    private void getInfoAboutLocation() {
        Location l = new Location();
        l.setBarCodeLocation(CHANGE_LOCATION_RESULT_CODE);
        Rest.getRest().getProductsFromLocation(Rest.token, l).enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful() && response.body() != null) {
                    location = response.body();
                    showDialog();
                } else {
                    Toast.makeText(getContext(), "Brak produktów na tej lokalizacji", Toast.LENGTH_SHORT).show();
                    CHANGE_LOCATION_RESULT_CODE = "";
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Toast.makeText(getContext(), "Brak produktów na tej lokalizacji", Toast.LENGTH_SHORT).show();
                CHANGE_LOCATION_RESULT_CODE = "";
            }
        });

    }

    private void showDialog() {

        if (location.getProducts() != null) {
            tookProducts.clear();
            List<SerializedProduct> productsOnLocation = location.getProducts();
            final List<ProductToTake> onLocation = new ArrayList<>();

            for (SerializedProduct serializedProduct : productsOnLocation) {
                onLocation.add(new ProductToTake(serializedProduct, serializedProduct.getState()));
            }

            final TakeProductDialog takeProductDialog = new TakeProductDialog(getContext(), location.getBarCodeLocation(), false);
            takeProductDialog.setProductToTakes(onLocation);
            takeProductDialog.show();
            takeProductDialog.setCanceledOnTouchOutside(false);
            takeProductDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.tpd_accept:
                            takeProductDialog.dismiss();
                            for (ProductToTake productToTake : onLocation) {
                                if (productToTake.getTookCount() > 0) {
                                    SerializedProduct product = new SerializedProduct();
                                    product.setId(productToTake.getProduct().getId());
                                    product.setState(productToTake.getTookCount());
                                    Product p = new Product();
                                    p.setBarCode(productToTake.getProduct().getProduct().getBarCode());
                                    product.setProduct(p);
                                    tookProducts.add(product);
                                }
                            }
                            setFirst_location();
                            break;
                        case R.id.tpd_cancel:
                            takeProductDialog.dismiss();
                            CHANGE_LOCATION_RESULT_CODE = "";
                            break;
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Brak produktu na lokalizacji", Toast.LENGTH_SHORT).show();
            CHANGE_LOCATION_RESULT_CODE = "";
        }
    }

    private void changeLocation() {
        Location newLocation = new Location();
        newLocation.setProducts(tookProducts);
        newLocation.setBarCodeLocation(CHANGE_LOCATION_RESULT_CODE2);

        Location oldLocation = new Location();
        oldLocation.setBarCodeLocation(CHANGE_LOCATION_RESULT_CODE);

        LocationsToChange locationsToChange = new LocationsToChange(newLocation, oldLocation);

        Rest.getRest().changeLocation(Rest.token, locationsToChange).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println(response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });

    }

}
