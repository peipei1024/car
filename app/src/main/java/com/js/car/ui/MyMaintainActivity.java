package com.js.car.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.Car;
import com.js.car.bean.CarFault;
import com.js.car.bean.IllegalInf;
import com.js.car.bean.User;
import com.js.car.ui.illegalreslut.IllegalReslutAdapter;
import com.js.car.utils.ToastUtils;
import com.js.car.view.PullUpMoreListView;
import com.js.car.view.SwipeRefresh;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/*
* Class name :MyMaintainActivity
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
public class MyMaintainActivity extends Activity implements PullUpMoreListView.ILoadListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_progressbar)
    ProgressBar idProgressbar;


    @Bind(R.id.id_listview)
    PullUpMoreListView idListview;
    @Bind(R.id.id_swiperefresh)
    SwipeRefresh idSwiperefresh;
    User user;

    private MyMaintainAdapter adapter;
    private List<CarFault> illegalInfList = new ArrayList<CarFault>();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.toast(MyMaintainActivity.this, "暂无维护信息");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_maintain);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        user = BmobUser.getCurrentUser(this, User.class);
        init();
        getMyCarFault();


    }

    @OnClick(R.id.id_back_arrow_image)
    public void car() {
        ActivityCollector.removeActivity(this);
    }

    private void init() {

        adapter = new MyMaintainAdapter(this, illegalInfList);

        idListview.setAdapter(adapter);
        idListview.setLoadListener(this);
        idSwiperefresh.setOnRefreshListener(this);
    }

    @Override
    public void onLoad() {
        //上拉更多
    }

    @Override
    public void onRefresh() {
        //下拉刷新
        getMyCarFault();
    }

    @OnItemClick(R.id.id_listview)
    public void openDetails(int position) {
        //listview点击事件
    }

    /**
     * 得到我的汽车维护列表
     */
    public void getMyCarFault() {

        BmobQuery<CarFault> query = new BmobQuery<CarFault>();

        query.addWhereEqualTo("user", user);    // 查询当前用户的所有帖子
        query.findObjects(this, new FindListener<CarFault>() {
            @Override
            public void onSuccess(List<CarFault> object) {
                // TODO Auto-generated method stub
                //carlist = (ArrayList) object;
                illegalInfList.clear();
                illegalInfList.addAll(object);
                Message mes = new Message();
                mes.what = 1;
                handler.sendMessage(mes);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.toast(MyMaintainActivity.this, msg);
            }
        });

    }
}
