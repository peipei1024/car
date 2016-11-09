package com.js.car.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.Time;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.utils.IntentUtils;
import com.js.car.utils.ToastUtils;
import com.zxing.activity.CaptureActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 景贝贝 on 2016/5/9.
 */
public class LoginActivity extends Activity{
    @Bind(R.id.id_phonel_edit)
    EditText idPhoneEdit;
    @Bind(R.id.id_passwordl_edit)
    EditText idPasswordEdit;
    @Bind(R.id.id_login_btn)
    Button idLoginBtn;
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);


    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }

    @OnClick({R.id.id_back_arrow_image,R.id.id_login_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_back_arrow_image:
                ActivityCollector.removeActivity(this);
                break;

            case R.id.id_login_btn://登陆
                BmobUser bu2 = new BmobUser();
                String phone=idPhoneEdit.getText().toString();
                String password=idPasswordEdit.getText().toString();
                if(phone==null||password==null){
                    ToastUtils.toast(LoginActivity.this,"手机号或密码不完整");
                    idLoginBtn.setClickable(true);
                    break;
                }
                idLoginBtn.setText("Loding..");
                idLoginBtn.setClickable(false);
                bu2.setUsername(phone);
                bu2.setPassword(password);
                bu2.login(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.toast(LoginActivity.this,"登录成功:");
                        IntentUtils.doIntent(LoginActivity.this,BaseActivity.class);
                        //通过BmobUser user = BmobUser.getCurrentUser(context)获取登录成功后的本地用户信息
                        //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(context,MyUser.class)获取自定义用户信息
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        ToastUtils.toast(LoginActivity.this,"登录失败:"+msg);
                        idLoginBtn.setClickable(true);
                    }
                });
                break;

        }
    }


}
