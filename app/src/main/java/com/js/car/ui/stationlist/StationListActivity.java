package com.js.car.ui.stationlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.Station;
import com.js.car.ui.station.StationActivity;
import com.js.car.utils.GetXxxMethod;
import com.js.car.utils.ToastUtils;
import com.js.car.view.PullUpMoreListView;
import com.js.car.view.SwipeRefresh;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/*
* Class name :StationListActivity
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-4.
*
*/
public class StationListActivity extends Activity implements PullUpMoreListView.ILoadListener, SwipeRefreshLayout.OnRefreshListener, AMapLocationListener {
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_listview)
    PullUpMoreListView idListview;
    @Bind(R.id.id_swiperefresh)
    SwipeRefresh idSwiperefresh;

    private StationListAdapter adapter;
    private ArrayList<Station> list = new ArrayList<>();
    private String myLocation = null;
    private AMapLocationClient mlocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocation loc;
    private int count = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1) {
                loc = (AMapLocation) msg.obj;
                if (loc != null && count == 0) {
                    myLocation = loc.getCity();
                    display();
                    count = 1;
                }
            }
            if (msg.what == 1) {
                idSwiperefresh.setRefreshing(false);
                adapter.refreshDta(list);
            } else {
                ToastUtils.toast(StationListActivity.this, "本地区暂无加油站");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        init();
        idSwiperefresh.setRefreshing(true);

//        list = Test.test();

//        adapter.refreshDta(list);
    }

    public void display() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = (ArrayList<Station>) GetXxxMethod.getRegionStationMethod(myLocation);
                Message ms = new Message();
                if (list.size() != 0) {
                    ms.what = 1;
                }
                handler.sendMessage(ms);
            }
        }).start();
    }

    @OnItemClick(R.id.id_listview)
    public void openStation(int position) {
        //ToastUtils.toast(this, position + "");
        Intent intent = new Intent(this, StationActivity.class);
        intent.putExtra("station", list.get(position));
        startActivity(intent);

//        IntentUtils.doIntentWithBundle(this,StationActivity.class,bundle);
//        IntentUtils.doIntentWithString(this, StationActivity.class, "id", list.get(position).getId());
    }

    private void init() {
        adapter = new StationListAdapter(list, this);
        idListview.setLoadListener(this);
        idSwiperefresh.setOnRefreshListener(this);
        idListview.setAdapter(adapter);

        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @OnClick(R.id.id_back_arrow_image)
    public void back() {
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onLoad() {
        //上拉更多
        idSwiperefresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        //下拉刷新
        idListview.loadComplete();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mlocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mlocationClient.onDestroy();
            mlocationClient = null;
            mLocationOption = null;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Message message = new Message();
        message.what = 0x1;
        message.obj = aMapLocation;
        handler.sendMessage(message);
    }
}
