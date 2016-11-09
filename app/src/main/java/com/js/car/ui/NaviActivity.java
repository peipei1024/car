package com.js.car.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* Class name :NaviActivity
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-6.
*
*/
public class NaviActivity extends Activity {
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_start_edit)
    EditText idStartEdit;
    @Bind(R.id.id_exchange_image)
    ImageView idExchangeImage;
    @Bind(R.id.id_stop_edit)
    EditText idStopEdit;
    @Bind(R.id.id_app_view)
    LinearLayout idAppView;
    @Bind(R.id.id_phone_view)
    LinearLayout idPhoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.id_back_arrow_image, R.id.id_start_edit, R.id.id_exchange_image, R.id.id_stop_edit, R.id.id_app_view, R.id.id_phone_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_back_arrow_image:
                ActivityCollector.removeActivity(this);
                break;
            case R.id.id_start_edit:
                break;
            case R.id.id_exchange_image:
                ToastUtils.toast(this, "交换位置");
                break;
            case R.id.id_stop_edit:
                break;
            case R.id.id_app_view:
                ToastUtils.toast(this, "使用app地图");
                break;
            case R.id.id_phone_view:
                ToastUtils.toast(this, "使用本机地图");
                break;
        }
    }
}
