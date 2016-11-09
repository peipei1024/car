package com.js.car.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.User;
import com.js.car.maptest.ToastUtil;
import com.js.car.utils.IntentUtils;
import com.js.car.utils.ToastUtils;


/*
* Class name :SettingActivity
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-4-15.
*
*/
public class SettingActivity extends Activity{
    private TextView exit_text;
    private ImageView image_arrow_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActivityCollector.addActivity(this);
        findViewById(R.id.help_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.doIntent(SettingActivity.this, HelpActivity.class);
            }
        });
        findViewById(R.id.newapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast(SettingActivity.this, "已经是最新版本");
            }
        });
        findViewById(R.id.about_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.doIntent(SettingActivity.this, AboutActivity.class);
            }
        });
       exit_text= (TextView) findViewById(R.id.exit_text);
        image_arrow_back = (ImageView) findViewById(R.id.id_back_arrow_image);

        image_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        exit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logOut(SettingActivity.this);
                ToastUtils.toast(SettingActivity.this,"退出成功");
               int j= ActivityCollector.list.size();
                for (int i=ActivityCollector.list.size();i!=0;i--){
                    ActivityCollector.list.get(i).finish();
                }

            }
        });
    }
}
