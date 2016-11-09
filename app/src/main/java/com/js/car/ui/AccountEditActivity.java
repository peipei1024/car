package com.js.car.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;


/*
* Class name :AccountEditActivity
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
public class AccountEditActivity extends Activity implements View.OnClickListener {

    private ImageView text_back_arrow;
    private EditText edit_name;
    private Button button_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        ActivityCollector.addActivity(this);
        init();
    }

    private void init() {
        text_back_arrow= (ImageView) findViewById(R.id.id_image_back_arrow);
        edit_name= (EditText) findViewById(R.id.id_edit_name);
        button_save= (Button) findViewById(R.id.id_button_save);

        text_back_arrow.setOnClickListener(this);
        button_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_image_back_arrow:
                finish();
                break;
            case R.id.id_button_save:
                Intent intent=new Intent();
                String s=edit_name.getText().toString();
                intent.putExtra("name",edit_name.getText().toString());
                setResult(2,intent);
                finish();
                break;
        }
    }
}

