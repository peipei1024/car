package com.js.car.maptest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.js.car.R;

import java.util.ArrayList;

/**
 * Created by JiaM on 2016/5/16.
 */
public class RoutePlanningActivity extends Activity implements View.OnClickListener, AMapNaviListener, AMapLocationListener, LocationSource {

    private LinearLayout local_navi;
    private LinearLayout gaode_navi;
    private ImageView route_planning_back;
    //起点终点坐标
    private NaviLatLng mNaviStart;
    private NaviLatLng mNaviEnd;
    // 起点终点列表0
    private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
    private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
    //地图和导航资源
    private MapView mMapView;
    private AMap mAmap;
    //规划线路
    private RouteOverLay mRouteoverLay;
    private AMapNavi aMapNavi;
    private UiSettings mUiSetting;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocation loc;
    private Double start_Longitude;
    private Double start_Latitude;
    private Double end_Longitude;
    private Double end_Latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        aMapNavi = AMapNavi.getInstance(this);
        aMapNavi.addAMapNaviListener(this);
        aMapNavi.setEmulatorNaviSpeed(150);

        setContentView(R.layout.activity_route_planning);
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {

        if (mAmap == null) {
            mAmap = mMapView.getMap();
            mUiSetting = mAmap.getUiSettings();
        }
        local_navi = (LinearLayout) findViewById(R.id.local_navi);
        gaode_navi = (LinearLayout) findViewById(R.id.gaode_navi);
        route_planning_back = (ImageView) findViewById(R.id.route_planning_back);
        Intent point = getIntent();
        start_Longitude = point.getDoubleExtra("start_Longitude", 0);
        start_Latitude = point.getDoubleExtra("start_Latitude", 0);
        end_Longitude = point.getDoubleExtra("end_Longitude", 0);
        end_Latitude = point.getDoubleExtra("end_Latitude", 0);
        mNaviStart = new NaviLatLng(start_Latitude, start_Longitude);
        mNaviEnd = new NaviLatLng(end_Latitude, end_Longitude);

        mRouteoverLay = new RouteOverLay(mAmap, null);
        route_planning_back.setOnClickListener(this);
        local_navi.setOnClickListener(this);
        gaode_navi.setOnClickListener(this);

        mUiSetting.setCompassEnabled(true);
        mAmap.setLocationSource(this);
        mUiSetting.setMyLocationButtonEnabled(true);
        mAmap.setMyLocationEnabled(true);
        //locationToggle();
        calculateDriveRoute();
    }

    private void locationToggle() {
        mAmap.setLocationSource(this);
        mUiSetting.setMyLocationButtonEnabled(true);
        mAmap.setMyLocationEnabled(true);
    }

    //计算驾车路线
    private void calculateDriveRoute() {
        // Toast.makeText(this, mNaviStart.getLongitude() + "/" + mNaviStart.getLatitude() + "3" + mNaviEnd.getLongitude() + "/" + mNaviEnd.getLatitude(), Toast.LENGTH_LONG).show();
        mStartPoints.clear();
        mEndPoints.clear();
        mStartPoints.add(mNaviStart);
        mEndPoints.add(mNaviEnd);
        boolean isSuccess = aMapNavi.calculateDriveRoute(mStartPoints,
                mEndPoints, null, PathPlanningStrategy.DRIVING_DEFAULT);
        //Toast.makeText(this, isSuccess + "", Toast.LENGTH_SHORT).show();
        if (!isSuccess) {
            Toast.makeText(this, "路线计算失败,检查参数情况", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mStartPoints.add(mNaviStart);
        mEndPoints.add(mNaviEnd);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        aMapNavi.destroy();
    }

    @Override
    public void onCalculateRouteSuccess() {
        AMapNaviPath naviPath = aMapNavi.getNaviPath();
        if (naviPath == null) {
            return;
        }
        // 获取路径规划线路，显示到地图上
        mRouteoverLay.setAMapNaviPath(naviPath);
        mRouteoverLay.addToMap();
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        ToastUtil.show(this, "路径规划错误");
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    public void startAMapNavi(LatLng destinationXY) {
        //构造导航参数
        NaviPara naviPara = new NaviPara();
        //设置终点位置
        naviPara.setTargetPoint(destinationXY);
        //设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);
        try {
            //调起高德地图导航
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {
            //如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_navi:
                Intent localNavi = new Intent(RoutePlanningActivity.this, LocalNaviActivity.class);
                localNavi.putExtra("start_Longitude", start_Longitude);
                localNavi.putExtra("start_Latitude", start_Latitude);
                localNavi.putExtra("end_Longitude", end_Longitude);
                localNavi.putExtra("end_Latitude", end_Latitude);
                startActivity(localNavi);
                break;
            case R.id.gaode_navi:
                LatLng destinationXY = new LatLng(mNaviEnd.getLatitude(), mNaviEnd.getLongitude());
                startAMapNavi(destinationXY);
                break;
            case R.id.route_planning_back:
                finish();
                break;
        }
    }

    //定位监听
    @Override
    public void activate(OnLocationChangedListener listener) {
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


    //定位成功后回调函数
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null) {
            mListener.onLocationChanged(aMapLocation);
        }
    }
}
