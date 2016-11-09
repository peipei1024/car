package com.js.car.ui.mycar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.Car;
import com.js.car.bean.User;
import com.js.car.ui.BindCarActivity;
import com.js.car.utils.IntentUtils;
import com.js.car.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/*
* Class name :MyCarActivity
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
public class MyCarActivity extends Activity {
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_listview)
    ListView idListview;
    ArrayList<Car> carlist = new ArrayList<>();
    User user;
    MyCarAdapter myCarAdapter;

    private CardView cardView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                myCarAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.toast(MyCarActivity.this, "暂无车辆信息");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);
        user = BmobUser.getCurrentUser(this, User.class);
        getMyCar();
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        myCarAdapter = new MyCarAdapter(carlist, this);
        idListview.setAdapter(myCarAdapter);
        idListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MyCarActivity.this,EditMyCarActivity.class);
                intent.putExtra("car",carlist.get(position));
                startActivity(intent);

            }
        });

    }

    @OnClick(R.id.id_back_arrow_image)
    public void onClick() {
        ActivityCollector.removeActivity(this);
    }

    /**
     * 得到我的汽车列表
     */
    public void getMyCar() {

        BmobQuery<Car> query = new BmobQuery<Car>();

        query.addWhereEqualTo("user", user);    // 查询当前用户的所有帖子
        query.findObjects(this, new FindListener<Car>() {
            @Override
            public void onSuccess(List<Car> object) {
                // TODO Auto-generated method stub
                //carlist = (ArrayList) object;
                carlist.addAll(object);
                Message mes = new Message();
                mes.what = 1;
                handler.sendMessage(mes);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.toast(MyCarActivity.this, msg);
            }
        });

    }

}
