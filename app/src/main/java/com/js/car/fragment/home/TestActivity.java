package com.js.car.fragment.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.js.car.R;
import com.js.car.utils.IntentUtils;



/**
 * Created by 景贝贝 on 2016/6/12.
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        Button jump = (Button) findViewById(R.id.jump);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                IntentUtils.doIntent(TestActivity.this, HomeActivity.class);
            }
        });
    }
}
