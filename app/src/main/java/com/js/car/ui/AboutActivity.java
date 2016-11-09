package com.js.car.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.js.car.R;

/**
 * Created by 景贝贝 on 2016/6/14.
 */
public class AboutActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.id_back_arrow_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
