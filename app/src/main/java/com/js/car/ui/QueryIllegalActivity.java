package com.js.car.ui;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* Class name :QueryIllegalActivity
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
public class QueryIllegalActivity extends Activity {
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_webview)
    WebView idWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_illegal);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        idWebview.loadUrl("http://www.cheshouye.com/api/weizhang/?t=8296eb");
    }

    @OnClick(R.id.id_back_arrow_image)
    public void onClick() {
        ActivityCollector.removeActivity(this);
    }
}
