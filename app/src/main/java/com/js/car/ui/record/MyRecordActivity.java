package com.js.car.ui.record;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.CarFault;
import com.js.car.maptest.RecordSQLiteOpenHelper;
import com.js.car.maptest.RecordUtil;
import com.js.car.maptest.SearchRecord;
import com.js.car.ui.MyMaintainAdapter;
import com.js.car.utils.ToastUtils;
import com.js.car.view.PullUpMoreListView;
import com.js.car.view.SwipeRefresh;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* Class name :MyRecordActivity
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-8.
*
*/
public class MyRecordActivity extends Activity implements PullUpMoreListView.ILoadListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_listview)
    PullUpMoreListView idListview;
    @Bind(R.id.id_swiperefresh)
    SwipeRefresh idSwiperefresh;
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);
    private SQLiteDatabase db;
    private MyRecordAadapter adapter;
    List<RecordUtil> queryList = new ArrayList<RecordUtil>();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.toast(MyRecordActivity.this, "暂无行车记录");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_record);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        init();
        queryData();
    }

    private void init() {
        adapter = new MyRecordAadapter(this, queryList);

        idListview.setAdapter(adapter);
        idListview.setLoadListener(this);
        idSwiperefresh.setOnRefreshListener(this);
    }

    @OnClick(R.id.id_back_arrow_image)
    public void onClick() {
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onLoad() {
        queryData();
    }

    @Override
    public void onRefresh() {

    }

    /**
     * 模糊查询数据
     */
    private void queryData() {
        Cursor cursor = helper.getReadableDatabase().rawQuery("select * from records", null);
        if(queryList.size()!=0){
            queryList.clear();
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {

                do {
                    String startName = cursor.getString(1);
                    String endname = cursor.getString(2);
                    double start_Longitude = cursor.getDouble(3);
                    double start_Latitude = cursor.getDouble(4);
                    double end_Longitude = cursor.getDouble(5);
                    double end_Latitude = cursor.getDouble(6);
                    String data = cursor.getString(7);
                    RecordUtil recordUtil = new RecordUtil();
                    recordUtil.setStartName(startName);
                    recordUtil.setEndname(endname);
                    recordUtil.setStart_Longitude(start_Longitude);
                    recordUtil.setStart_Latitude(start_Latitude);
                    recordUtil.setEnd_Longitude(end_Longitude);
                    recordUtil.setEnd_Latitude(end_Latitude);
                    recordUtil.setData(data);
                    queryList.add(recordUtil);
                } while (cursor.moveToNext());
                Message mes = new Message();
                mes.what = 1;
                handler.sendMessage(mes);
            }

        }
    }
}
