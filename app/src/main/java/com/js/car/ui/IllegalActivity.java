package com.js.car.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* Class name :IllegalActivity
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-7.
*
*/
public class IllegalActivity extends Activity {
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_car_num_text)
    TextView idCarNumText;
    @Bind(R.id.id_status_text)
    TextView idStatusText;
    @Bind(R.id.id_fen_text)
    TextView idFenText;
    @Bind(R.id.id_money_text)
    TextView idMoneyText;
    @Bind(R.id.id_officer_text)
    TextView idOfficerText;
    @Bind(R.id.id_time_text)
    TextView idTimeText;
    @Bind(R.id.id_road_text)
    TextView idRoadText;
    @Bind(R.id.id_city_text)
    TextView idCityText;
    @Bind(R.id.id_info_text)
    TextView idInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illegal);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_back_arrow_image)
    public void onClick() {
        ActivityCollector.removeActivity(this);
    }
}
