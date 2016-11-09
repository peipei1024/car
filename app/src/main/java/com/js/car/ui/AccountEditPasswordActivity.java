package com.js.car.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.User;
import com.js.car.utils.ToastUtils;

import java.net.URL;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/*
* Class name :AccountEditPasswordActivity
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-4-28.
*
*/
public class AccountEditPasswordActivity extends Activity implements View.OnClickListener {
    private Button button_save;
    private ImageView image_arrow_back;
    private EditText edit_oldpassword;
    private EditText edit_password1;
    private EditText edit_password2;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit_password);
        ActivityCollector.addActivity(this);
        user = BmobUser.getCurrentUser(this, User.class);
        init();

    }

    private void init() {
        button_save = (Button) findViewById(R.id.id_button_save);
        image_arrow_back = (ImageView) findViewById(R.id.id_image_back_arrow);
        edit_password1 = (EditText) findViewById(R.id.id_edit_password1);
        edit_password2 = (EditText) findViewById(R.id.id_edit_password2);
        edit_oldpassword= (EditText) findViewById(R.id.id_edit_oldpassword);

        button_save.setOnClickListener(this);
        image_arrow_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_button_save:
                if (edit_oldpassword.getText().toString()!=null&&edit_password2.getText().toString().equals(edit_password1.getText().toString())){
                    ModifyPassword(edit_oldpassword.getText().toString(),edit_password1.getText().toString());
                    ActivityCollector.removeActivity(this);
                }else {
                    ToastUtils.toast(this,"两次输入密码不一致！");

                }

                break;
            case R.id.id_image_back_arrow:
                ActivityCollector.removeActivity(this);
                break;
            default:
                break;
        }
    }

    private void ModifyPassword(String oldpass, final String newPass){
        BmobUser.updateCurrentUserPassword(AccountEditPasswordActivity.this, oldpass, newPass, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                ToastUtils.toast(AccountEditPasswordActivity.this, "密码修改成功，可以用新密码进行登录啦");
                User newUser = new User();
                newUser.setMypassword(newPass);
                updateUser(newUser);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.toast(AccountEditPasswordActivity.this, "密码修改失败："+msg+"("+code+")");
            }
        });

    }


    /**
     * 更新信息
     * @param newUser
     */
    private void updateUser(User newUser){
        newUser.update(AccountEditPasswordActivity.this,user.getObjectId(),new UpdateListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
               // ToastUtils.toast(AccountEditPasswordActivity.this,"更新用户信息成功");
            }
            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                //ToastUtils.toast(AccountEditPasswordActivity.this,"更新用户信息失败:"+msg);

            }
        });

    }

}
