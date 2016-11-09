package com.js.car.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.js.car.R;
import com.js.car.maptest.ToastUtil;
import com.js.car.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 景贝贝 on 2016/6/14.
 */
public class HelpActivity extends Activity{
    @Bind(R.id.id_back_arrow_image)
    ImageView back_arrow;
    @Bind(R.id.save_button)
    Button savebutton;
    @Bind(R.id.message_edit)
    EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.save_button)
    public void save(){
        edit.setText("");
        ToastUtils.toast(this, "提交完成");
    }
    @OnClick(R.id.id_back_arrow_image)
    public void back(){
        finish();
    }
}
