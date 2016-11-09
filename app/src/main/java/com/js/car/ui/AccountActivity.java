package com.js.car.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.User;
import com.js.car.utils.IntentUtils;
import com.js.car.utils.ToastUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/*
* Class name :AccountActivity
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
public class AccountActivity extends Activity implements View.OnClickListener {
    private int NAME_CODE = 0;
    private ImageView image_arrow_back;
    private RelativeLayout view_head;
    private RelativeLayout view_name;
    private RelativeLayout view_sex;
    private RelativeLayout view_password;
    private ImageView image_head;
    private TextView text_name;
    private TextView text_sex;
    private TextView text_phone;
    private TextView text_schoolnum;
    private User user;

    String ssex[] = {"男", "女"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ActivityCollector.addActivity(this);
        user = BmobUser.getCurrentUser(this, User.class);
        init();
    }




    private void init() {
        image_arrow_back = (ImageView) findViewById(R.id.id_back_arrow_image);
        view_head = (RelativeLayout) findViewById(R.id.id_view_head);
        view_name = (RelativeLayout) findViewById(R.id.id_view_name);
        view_sex = (RelativeLayout) findViewById(R.id.id_view_sex);
        image_head = (ImageView) findViewById(R.id.id_image_head);
        text_name = (TextView) findViewById(R.id.id_text_name);
        text_sex = (TextView) findViewById(R.id.id_sex_text);
        view_password = (RelativeLayout) findViewById(R.id.id_view_password);
        text_phone = (TextView) findViewById(R.id.id_text_phone);
        String nickname=user.getNickname();
        String headpic=user.getPicpath();
        String sex=user.getSex();
        String phonenum=user.getMobilePhoneNumber();
        if(nickname!=null){
            text_name.setText(nickname);
        }
        if (headpic!=null){
           Picasso.with(this).load(headpic).into(image_head);
        }
        if (sex!=null){
           text_sex.setText(sex);
        }
        if (phonenum!=null){
            text_phone.setText(phonenum);
        }



        image_arrow_back.setOnClickListener(this);
        view_password.setOnClickListener(this);
        view_head.setOnClickListener(this);
        view_name.setOnClickListener(this);
        view_sex.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.id_view_head://头像
                //参考修改昵称

                break;
            case R.id.id_view_name://昵称
                intent = new Intent(this, AccountEditActivity.class);
                startActivityForResult(intent, NAME_CODE);
                break;
            case R.id.id_view_sex:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("性别");
                builder.setSingleChoiceItems(ssex, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        text_sex.setText(ssex[which]);
                        User newUser = new User();
                        newUser.setSex((String) text_sex.getText());
                        updateUser(newUser);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.id_view_password:
                IntentUtils.doIntent(this, AccountEditPasswordActivity.class);
                break;
            case R.id.id_back_arrow_image:
                ActivityCollector.removeActivity(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NAME_CODE){
            if (resultCode == 2) {
                String s=data.getExtras().getString("name");
                text_name.setText(data.getExtras().getString("name"));
                User newUser = new User();
                newUser.setNickname((String) text_name.getText());
                updateUser(newUser);

            }
        }

    }

    /**
     * 上传头像，传入本地图片路径
     * @param picPath
     */
    private void ModifyHead(String picPath){
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(AccountActivity.this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
              String bmobpath =   bmobFile.getFileUrl(AccountActivity.this);
                User newUser = new User();
                newUser.setPicpath(bmobpath);
                updateUser(newUser);
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });

    }

    /**
     * 更新信息
     * @param newUser
     */
    private void updateUser(User newUser){
        newUser.update(AccountActivity.this,user.getObjectId(),new UpdateListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                ToastUtils.toast(AccountActivity.this,"更新用户信息成功");
            }
            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.toast(AccountActivity.this,"更新用户信息失败:"+msg);

            }
        });

    }


}


