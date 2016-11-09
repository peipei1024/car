package com.js.car.ui.myillegal;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.ui.IllegalActivity;
import com.js.car.utils.IntentUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* Class name :MyIllegalActivity
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
public class MyIllegalActivity extends Activity {
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_1)
    RelativeLayout id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_illegal);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        //切记把车牌号那列显示出来
    }

    @OnClick({R.id.id_back_arrow_image, R.id.id_1})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_1:
                IntentUtils.doIntent(this, IllegalActivity.class);
                break;
            case R.id.id_back_arrow_image:
                ActivityCollector.removeActivity(this);
                break;
        }

    }
}
