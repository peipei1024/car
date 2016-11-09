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

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.User;
import com.js.car.utils.IntentUtils;
import com.js.car.utils.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 景贝贝 on 2016/5/10.
 */
public class RegisterActivity extends Activity{
    @Bind(R.id.id_phone_edit)
    EditText idPhoneEdit;
    @Bind(R.id.id_password_edit)
    EditText idPasswordEdit;
    @Bind(R.id.id_yanzhengma_edit)
    EditText idYanzhengmaEdit;
    @Bind(R.id.id_getvnum_btn)
    Button idGetnumBtn;
    @Bind(R.id.id_register_btn)
    Button idLoginBtn;
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;

    private TimeCount mTiemTimeCount;
    //短信验证码内容 验证码是6位数字的格式
    private String strContent;
    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";

    //填写服务器号码
    private static final String SERVICECHECKNUM = "";

    //更新界面
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (idYanzhengmaEdit != null) {
                idYanzhengmaEdit.setText(strContent);
            }
        }

    };
    //监听短信广播
    private BroadcastReceiver smsReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                byte[] pdu = (byte[]) obj;
                SmsMessage sms = SmsMessage.createFromPdu(pdu);
                // 短信的内容
                String message = sms.getMessageBody();
                Log.d("TAG", "message     " + message);
                String from = sms.getOriginatingAddress();
                Log.d("TAG", "from     " + from);
                if (SERVICECHECKNUM.equals(from.toString().trim()) || TextUtils.isEmpty(SERVICECHECKNUM)) {
                    Time time = new Time();
                    time.set(sms.getTimestampMillis());
                    String time2 = time.format3339(true);
                    Log.d("TAG", from + "   " + message + "  " + time2);
                    strContent = from + "   " + message;
                    //mHandler.sendEmptyMessage(1);
                    if (!TextUtils.isEmpty(from)) {
                        String code = patternCode(message);
                        if (!TextUtils.isEmpty(code)) {
                            strContent = code;
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                } else {
                    return;
                }
            }

        }
    };
    /**
     * 匹配短信中间的6个数字（验证码等）
     *
     * @param patternContent
     * @return
     */
    private String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        mTiemTimeCount = new TimeCount(60000, 1000);

    }
    @OnClick({R.id.id_back_arrow_image,R.id.id_getvnum_btn,R.id.id_register_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_back_arrow_image:
                ActivityCollector.removeActivity(this);
                break;
            case R.id.id_getvnum_btn://发送验证码
                Log.d("TAG", " sendSms! ");
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.provider.Telephony.SMS_RECEIVED");
                filter.setPriority(Integer.MAX_VALUE);
                registerReceiver(smsReceiver, filter);
                mTiemTimeCount.start();
                BmobSMS.requestSMSCode(RegisterActivity.this, idPhoneEdit.getText().toString(),"短信验证码", new RequestSMSCodeListener() {

                    @Override
                    public void done(Integer smsId,BmobException ex) {
                        // TODO Auto-generated method stub
                        if(ex==null){//验证码发送成功
                            Log.i("smile", "短信id："+smsId);//用于后续的查询本次短信发送状态
                        }
                    }
                });

                break;
            case R.id.id_register_btn://注册
                RegisterMethod();

                break;

        }
    }
    public void RegisterMethod(){
        User user=new User();
        user.setUsername(idPhoneEdit.getText().toString());
        user.setPassword(idPasswordEdit.getText().toString());
        user.setMypassword(idPasswordEdit.getText().toString());
        user.setMobilePhoneNumber(idPhoneEdit.getText().toString());
        String yanzhengma=idYanzhengmaEdit.getText().toString();
        if (yanzhengma!=null){
            user.signOrLogin(RegisterActivity.this, yanzhengma, new SaveListener() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    ToastUtils.toast(RegisterActivity.this,"注册或登录成功");
                    IntentUtils.doIntent(RegisterActivity.this,BaseActivity.class);
                    // Log.i("smile", ""+user.getUsername()+"-"+user.getAge()+"-"+user.getObjectId());
                }

                @Override
                public void onFailure(int code, String msg) {
                    // TODO Auto-generated method stub
                    ToastUtils.toast(RegisterActivity.this,"错误码："+code+",错误原因："+msg);
                }
            });
        }else{
            ToastUtils.toast(RegisterActivity.this,"请输入验证码");
        }


    }
    //计时重发
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            idGetnumBtn.setClickable(false);
            idGetnumBtn.setText(millisUntilFinished / 1000 + "秒后重新发送");
            Spannable span = new SpannableString(idGetnumBtn.getText().toString());//获取按钮的文字
            span.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
            idGetnumBtn.setText(span);
        }

        @Override
        public void onFinish() {
            idGetnumBtn.setText("获取验证码");
            idGetnumBtn.setClickable(true);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(smsReceiver);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.onDestroy();
    }


}
