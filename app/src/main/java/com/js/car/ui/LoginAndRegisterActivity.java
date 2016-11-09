package com.js.car.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.User;
import com.js.car.utils.IntentUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

/*
* Class name :LoginAndRegisterActivity
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-9.
*
*/
public class LoginAndRegisterActivity extends Activity {
    @Bind(R.id.id_login_text)
    TextView idLoginText;
    @Bind(R.id.id_register_text)
    TextView idRegisterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, "e7ed0a59bad948144e5bb6879026c205");
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this);

        User userInfo = BmobUser.getCurrentUser(LoginAndRegisterActivity.this,User.class);
        if (userInfo!=null){
            IntentUtils.doIntent(this,BaseActivity.class);
        }
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(1);
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }

    @OnClick(R.id.id_login_text)
    public void onClick() {
        IntentUtils.doIntent(this, LoginActivity.class);
    }

    @OnClick(R.id.id_register_text)
    public void RegisterOnClick() {
        IntentUtils.doIntent(this, RegisterActivity.class);
    }
}
