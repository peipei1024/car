package com.js.car.maptest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.js.car.R;
import com.js.car.bean.Station;
import com.js.car.ui.station.StationActivity;
import com.js.car.utils.GetXxxMethod;

import java.util.List;

/**
 * Created by JiaM on 2016/5/9.
 */
public class GasStation extends FragmentActivity implements View.OnClickListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, PoiSearch.OnPoiSearchListener, LocationSource, AMapLocationListener {

    private String keyWord = "加油站";
    private String myLocation = null;
    private AMap aMap;
    private UiSettings mUiSetting;
    private TextView display;
    private ImageView gas_station_back;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private AMapLocation loc;
    private int count = 0;
    private Intent stationIntent;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0x1) {
                loc = (AMapLocation) msg.obj;
                myLocation = loc.getCity();
                if (myLocation != null && count == 0) {
                    doSearchQuery();
                    count = 1;
                }
            }
            if (msg.what==0x2){
                Station station= (Station) msg.obj;
             double  mylng=   loc.getLongitude();
                double mylat=loc.getLatitude();
                stationIntent.putExtra("station",station);
                stationIntent.putExtra("mylng",mylng);
                stationIntent.putExtra("mylat",mylat);
                startActivity(stationIntent);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gas_station);
        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            mUiSetting = aMap.getUiSettings();
            setUpMap();
        }
    }

    private void setUpMap() {
        display = (TextView) findViewById(R.id.display);
        gas_station_back = (ImageView) findViewById(R.id.gas_station_back);
        gas_station_back.setOnClickListener(this);
        display.setOnClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
        mUiSetting.setCompassEnabled(true);
        aMap.setLocationSource(this);
        mUiSetting.setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        //doSearchQuery();
    }

    private void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", myLocation);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gas_station_back:
                finish();
                break;
            case R.id.display:
                nextButton();
                break;
        }
    }

    private void nextButton() {
        if (query != null && poiSearch != null && poiResult != null) {

            if (poiResult.getPageCount() - 1 > currentPage) {
                currentPage++;
                display.setText("当前第" + (currentPage + 1) + "页(共" + poiResult.getPageCount() + "页)，点击加载下一页");
                query.setPageNum(currentPage);// 设置查后一页
                poiSearch.searchPOIAsyn();
            } else {
                ToastUtil.show(GasStation.this,
                        R.string.no_result);
            }
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.gas_station_display,
                null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());
        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 stationIntent = new Intent(GasStation.this,StationActivity.class);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    Station     station= GetXxxMethod.getOneStation(marker.getPosition().latitude,marker.getPosition().longitude);
                        Message ms=new Message();
                        ms.what=0x2;
                        ms.obj=station;
                        mHandler.sendMessage(ms);
                    }
                }).start();

//                stationIntent.putExtra("station",station);
////                intent.putExtra("longitude", marker.getPosition().longitude);
////                intent.putExtra("latitude", marker.getPosition().latitude);
//                startActivity(stationIntent);
            }
        });
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    display.setText("当前第" + (currentPage + 1) + "页(共" + poiResult.getPageCount() + "页)，点击加载下一页");
                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                    } else {
                        ToastUtil.show(GasStation.this,
                                R.string.no_result);
                    }
                }
            } else {
                ToastUtil.show(GasStation.this,
                        R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.show(GasStation.this,
                    R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.show(GasStation.this, R.string.error_key);
        } else {
            ToastUtil.show(GasStation.this, getString(R.string.error_other) + rCode);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        //Toast.makeText(this, "activate", Toast.LENGTH_SHORT).show();
        mListener = listener;
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

    @Override
    public void deactivate() {
        //Toast.makeText(this, "deactivate", Toast.LENGTH_SHORT).show();
        mListener = null;
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
        if (mListener != null) {
            mListener.onLocationChanged(aMapLocation);
        }
        Message message = new Message();
        message.what = 0x1;
        message.obj = aMapLocation;
        mHandler.sendMessage(message);
    }
}
