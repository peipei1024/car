package com.js.car.maptest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.js.car.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JiaM
 * on 2016/5/9.
 */

public class Navigation extends Activity implements View.OnClickListener, AMapLocationListener {
    private TextView start_location;
    private TextView end_location;
    private ImageView exchange;
    private TextView goCar;
    private ImageView id_back_arrow_image;
    private LinearLayout bt_cancel_history;
    private ProgressDialog progressDialog = null;
    private static String startLocation = "";
    private static String endLocation = "";
    private static String median = "";
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationClientOption = null;
    private AMapLocation loc;
    private static Double start_Longitude;
    private static Double start_Latitude;
    private static Double medianPointer;
    private static Double end_Longitude;
    private static Double end_Latitude;
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);

    private View tv;
    private SearchRecord searchRecord;
    private SQLiteDatabase db;
    private ListView navi_record;
    private List<RecordUtil> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_start);
        initView();
    }


    private void initView() {
        start_location = (TextView) findViewById(R.id.start_location);
        end_location = (TextView) findViewById(R.id.finish_location);
        exchange = (ImageView) findViewById(R.id.exchange);
        goCar = (TextView) findViewById(R.id.go_car);
        navi_record = (ListView) findViewById(R.id.navi_record);
        id_back_arrow_image = (ImageView) findViewById(R.id.id_back_arrow_image);
        goCar.setOnClickListener(this);
        exchange.setOnClickListener(this);

        start_location.setOnClickListener(this);
        end_location.setOnClickListener(this);
        id_back_arrow_image.setOnClickListener(this);
        showProgressDialog();
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationClientOption = new AMapLocationClientOption();
        //高精度模式
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationClientOption.setOnceLocation(false);
        //设置定位监听
        locationClient.setLocationListener(this);
        //设置定位参数
        locationClientOption.setNeedAddress(true);
        locationClientOption.setInterval(60000);
        if (start_location.getText().equals(getResources().getString(R.string.startLocation))) {
            locationClient.setLocationOption(locationClientOption);
            locationClient.startLocation();
        }
        tv = LayoutInflater.from(Navigation.this).inflate(R.layout.listview_footer, null);
        bt_cancel_history = (LinearLayout) tv.findViewById(R.id.cancel_history);
        bt_cancel_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                navi_record.setVisibility(View.GONE);
                searchRecord.notifyDataSetChanged();
            }
        });
        navi_record.addFooterView(tv);
        navi_record.setVisibility(View.INVISIBLE);
        navi_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<RecordUtil> mList = searchRecord.getList();
                start_location.setText(mList.get(position).getStartName());
                end_location.setText(mList.get(position).getEndname());
                start_Longitude = mList.get(position).getStart_Longitude();
                start_Latitude = mList.get(position).getStart_Latitude();
                end_Longitude = mList.get(position).getEnd_Longitude();
                end_Latitude = mList.get(position).getEnd_Latitude();
            }
        });
        queryData();
    }

    /*显示进度条*/
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在定位...");
        progressDialog.show();
    }

    /*隐藏进度条*/
    private void dissmissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置定位参数
        locationClientOption.setNeedAddress(true);
        locationClientOption.setGpsFirst(true);
        if (start_location.getText().equals(getResources().getString(R.string.startLocation))) {
            locationClient.setLocationOption(locationClientOption);
            locationClient.startLocation();
        }
        queryData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationClientOption = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            switch (requestCode) {
                case 0:
                    start_location.setText(data.getStringExtra("address"));
                    if (!start_location.getText().toString().equals("我的位置")) {
                        startLocation = data.getStringExtra("address");
                    }
                    start_Longitude = data.getDoubleExtra("Longitude", 0);
                    start_Latitude = data.getDoubleExtra("Latitude", 0);
                    doJump();
                    break;
                case 1:
                    end_location.setText(data.getStringExtra("address"));
                    endLocation = data.getStringExtra("address");
                    end_Longitude = data.getDoubleExtra("Longitude", 0);
                    end_Latitude = data.getDoubleExtra("Latitude", 0);
                    doJump();
                    break;

            }
        }
    }

    /*跳转导航*/
    public void doJump() {
        searchRecord.notifyDataSetChanged();
        if ((!"".equals(start_location.getText().toString())) && (!"".equals(end_location.getText().toString()))) {
            String start = start_location.getText().toString();
            String end = end_location.getText().toString();
            boolean hasData = hasData(start.trim(), end.trim());
            if (!hasData) {
                RecordUtil recorddata = new RecordUtil();
                recorddata.setStartName(start);
                recorddata.setEndname(end);
                recorddata.setStart_Longitude(start_Longitude);
                recorddata.setStart_Latitude(start_Latitude);
                recorddata.setEnd_Longitude(end_Longitude);
                recorddata.setEnd_Latitude(end_Latitude);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                recorddata.setData(str);
                insertData(recorddata);
                queryData();
            }
            //insertData(start, end, start_Longitude, start_Latitude, end_Longitude, end_Latitude);
            Intent routePlanning = new Intent(this, RoutePlanningActivity.class);
            routePlanning.putExtra("start_Longitude", start_Longitude);
            routePlanning.putExtra("start_Latitude", start_Latitude);
            routePlanning.putExtra("end_Longitude", end_Longitude);
            routePlanning.putExtra("end_Latitude", end_Latitude);
            startActivity(routePlanning);
        } else if ("".equals(start_location.getText().toString())) {
            ToastUtil.show(Navigation.this, "请输入起点");
        } else if ("".equals(end_location.getText().toString())) {
            ToastUtil.show(Navigation.this, "请输入终点");
        }
    }

    /**
     * 插入数据
     */
    private void insertData(RecordUtil recorddata) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("startName", recorddata.getStartName());
        contentValues.put("endname", recorddata.getEndname());
        contentValues.put("start_Longitude", recorddata.getStart_Longitude());
        contentValues.put("start_Latitude", recorddata.getStart_Latitude());
        contentValues.put("end_Longitude", recorddata.getEnd_Longitude());
        contentValues.put("end_Latitude", recorddata.getEnd_Latitude());
        contentValues.put("date",recorddata.getData());
        db.insert("records", null, contentValues);
        db.close();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }

    /**
     * 模糊查询数据
     */
    private void queryData() {
        Cursor cursor = helper.getReadableDatabase().rawQuery("select * from records", null);
        List<RecordUtil> queryList = new ArrayList<RecordUtil>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                navi_record.setVisibility(View.VISIBLE);
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
            }
            searchRecord = new SearchRecord(Navigation.this, queryList);
            navi_record.setAdapter(searchRecord);
            searchRecord.notifyDataSetChanged();
        }
    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String start, String end) {
        boolean isHasData = false;
        Cursor cursor = helper.getReadableDatabase().rawQuery("select * from records", null);
        if (cursor != null) {
            list = new ArrayList<RecordUtil>();
            if (cursor.moveToFirst()) {
                do {
                    String startName = cursor.getString(1);
                    String endname = cursor.getString(2);
                    if (startName.equals(start) && endname.equals(end)) {
                        isHasData = true;
                        break;
                    }
                } while (cursor.moveToNext());
            }
        }
        return isHasData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_location:
                Intent start = new Intent(this, ChooseLocation.class);
                start.putExtra("hintText", "输入起点");
                startActivityForResult(start, 0);
                break;
            case R.id.finish_location:
                Intent end = new Intent(this, ChooseLocation.class);
                end.putExtra("hintText", "输入终点");
                startActivityForResult(end, 1);
                break;
            case R.id.exchange:
                //ToastUtil.show(Navigation.this, "test");
                exchangeLocation();
                break;
            case R.id.go_car:
                doJump();
                break;
            case R.id.id_back_arrow_image:
                finish();
                break;
        }
    }

    //起点、终点互换
    public void exchangeLocation() {
        //ToastUtil.show(Navigation.this, startLocation + "/" + median + "/" + endLocation);
        median = startLocation;
        startLocation = endLocation;
        endLocation = median;
        median = null;
        medianPointer = start_Longitude;
        start_Longitude = end_Longitude;
        end_Longitude = medianPointer;

        medianPointer = start_Latitude;
        start_Latitude = end_Latitude;
        end_Latitude = medianPointer;
        medianPointer = null;
        if (start_location.getText().toString().equals("我的位置")) {
            end_location.setText("我的位置");
            start_location.setText(startLocation);
        } else if (end_location.getText().toString().equals("我的位置")) {
            end_location.setText(endLocation);
            start_location.setText("我的位置");
        } else {
            end_location.setText(endLocation);
            start_location.setText(startLocation);
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case Utils.MSG_LOCATION_FINISH:
                    loc = (AMapLocation) msg.obj;
                    //得到的位置信息
                    String result = Utils.getLocationStr(loc);
                    start_Longitude = loc.getLongitude();
                    start_Latitude = loc.getLatitude();
                    if (start_location.getText().toString().equals("我的位置")) {
                        startLocation = loc.getPoiName();
                    } else if (end_location.getText().toString().equals("我的位置")) {
                        endLocation = loc.getPoiName();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            dissmissProgressDialog();
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }
    }
}
