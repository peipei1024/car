package com.js.car.maptest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.js.car.R;

/**
 * Created by JiaM on 2016/5/22.
 */
public class GasStationInformation extends Activity {
    private Double latitude;//纬度
    private Double longitude;//经度
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gas_station_information);
        Intent intent = getIntent();
        longitude = intent.getDoubleExtra("longitude", 0);
        latitude = intent.getDoubleExtra("latitude", 0);
        textView = (TextView) findViewById(R.id.display);
        textView.setText("经度" + longitude + "纬度" + latitude);
    }
}
