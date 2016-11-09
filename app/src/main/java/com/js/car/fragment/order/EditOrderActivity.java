package com.js.car.fragment.order;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.Order;
import com.js.car.utils.ToastUtils;
import com.zxing.encoding.EncodingHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 景贝贝 on 2016/5/22.
 */
public class EditOrderActivity extends Activity {
    private TextView ordername;//
    private TextView orderaddress;
    private ImageView backIV;
    private TextView ordertotal;
    private TextView orderoiltype;
    private TextView ordertime;
    private ImageView orderpic;
    private Order myorder;
    private TextView determine;
    private String CoderMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myorder = (Order) getIntent().getSerializableExtra("myorder");
        Log.i("订单详情", myorder.getStationName());
        setContentView(R.layout.activity_order);
        ActivityCollector.addActivity(this);
        init();
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.removeActivity(EditOrderActivity.this);
            }
        });
        determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myorder.setCoderMessage(CoderMessage);
                Order order=new Order();
                order.setObjectId(myorder.getObjectId());
                order.delete(EditOrderActivity.this, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.toast(EditOrderActivity.this,"删除成功！");
                        ActivityCollector.removeActivity(EditOrderActivity.this);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtils.toast(EditOrderActivity.this,"删除失败！");
                        ActivityCollector.removeActivity(EditOrderActivity.this);
                    }
                });

            }
        });

    }

    public void init() {
        ordername = (TextView) findViewById(R.id.id_order_name_text);
        orderaddress = (TextView) findViewById(R.id.id_order_address_text);
        ordertotal = (TextView) findViewById(R.id.id_order_total_text);
        orderoiltype = (TextView) findViewById(R.id.id_order_oiltype_text);
        ordertime = (TextView) findViewById(R.id.id_order_time_text);
        orderpic = (ImageView) findViewById(R.id.id_order_code_iv);
        determine = (TextView) findViewById(R.id.id_order_determine);
        backIV = (ImageView) findViewById(R.id.id_back_arrow_image);
        determine.setText("删除");


        ordername.setText(myorder.getStationName());
        orderaddress.setText(myorder.getStationAddress());
        ordertotal.setText(myorder.getTotal() + "元");
        orderoiltype.setText(myorder.getOilType());

        ordertime.setText(myorder.getCreatedAt());
        CoderMessage = ordername.getText() + "，" + orderaddress.getText() + "，" + ordertotal.getText() + "，" + orderoiltype.getText() + "，" + ordertime.getText();

        try {

            if (!CoderMessage.equals("")) {
                //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
                Bitmap qrCodeBitmap = EncodingHandler.createQRCode(CoderMessage, 350);
                orderpic.setImageBitmap(qrCodeBitmap);
            } else {
                Toast.makeText(EditOrderActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
            }

        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
