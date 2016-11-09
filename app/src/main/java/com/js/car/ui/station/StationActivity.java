package com.js.car.ui.station;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.Order;
import com.js.car.bean.Station;
import com.js.car.bean.User;
import com.js.car.maptest.RoutePlanningActivity;
import com.js.car.ui.OrderActivity;
import com.js.car.utils.LogUtils;
import com.js.car.utils.ToastUtils;
import com.js.car.view.BannerImg;
import com.js.car.view.PullUpMoreListView;



import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/*
* Class name :StationActivity
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
public class StationActivity extends Activity {
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_share_image)
    ImageView idShareImage;
    @Bind(R.id.id_more_image)
    ImageView idMoreImage;
    @Bind(R.id.id_station_banner)
    BannerImg idStationBanner;
    @Bind(R.id.id_name_text)
    TextView idNameText;
    @Bind(R.id.id_starts_ratingbar)
    RatingBar idStartsRatingbar;
    @Bind(R.id.id_start_text)
    TextView idStartText;
    @Bind(R.id.id_address_text)
    TextView idAddressText;
    @Bind(R.id.id_phone_image)
    ImageView idPhoneImage;
    @Bind(R.id.id_book_oil_button)
    Button idBookOilButton;
    @Bind(R.id.id_listview)
    PullUpMoreListView idListview;
    @Bind(R.id.id_go_view)
    LinearLayout idGoView;
    private StationAdapter adapter;
    ArrayList alist = null;
    ListViewForScrollView lsview;

    private TextView id1Text;
    private TextView id10Text;
    private TextView id20Text;
    private TextView id50Text;
    private TextView id100Text;
    private TextView id200Text;
    private TextView id500Text;
    private TextView id1000Text;
    private TextView idMoneyText;
    private int whichCheck = 0;

    private String id;
    private String phone = "12312312312";
    private final static String TAG = StationActivity.class.getClass().getName();
    private Station station = new Station();
    private String orderoilname;
    private User user;
    private double mylng;
    private double mylat;
    private Location lc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        lsview = (ListViewForScrollView) findViewById(R.id.staion_price_listview);
        Intent intent = getIntent();
        station = (Station) intent.getSerializableExtra("station");
        mylng = intent.getDoubleExtra("mylng", 0);
        mylat = intent.getDoubleExtra("mylat", 0);
        if (mylng == 0.0) {
            getLocationMethod();
            mylng = Double.valueOf(station.getLon());
            mylat = Double.valueOf(station.getLat());
        }
        alist = station.getGastprice();
        adapter = new StationAdapter(alist, this);
        lsview.setAdapter(adapter);

        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        user = BmobUser.getCurrentUser(this, User.class);
        init();
    }

    private void init() {
        idNameText.setText(station.getName());
        idAddressText.setText(station.getAddress());
        idStartsRatingbar.setNumStars(station.getStarts());
        idStartText.setText(station.getStarts() + "分");
        idStationBanner.setImageUris(station.getStationurls());
getLocationMethod();

    }

    @OnClick({R.id.id_back_arrow_image, R.id.id_share_image, R.id.id_more_image, R.id.id_phone_image, R.id.id_book_oil_button, R.id.id_go_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_back_arrow_image:
                ActivityCollector.removeActivity(this);
                break;
            case R.id.id_share_image:
                break;
            case R.id.id_more_image:
                break;
            case R.id.id_phone_image:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.id_book_oil_button:
                showPopupWindow(idBookOilButton, 6.79f);//第二个参数是油价
                break;
            case R.id.id_go_view:
                double[] tude = new double[2];
                tude = bd09togcj02(Double.valueOf(station.getLon()), Double.valueOf(station.getLat()));
                Intent routePlanning = new Intent(this, RoutePlanningActivity.class);
                routePlanning.putExtra("start_Longitude", lc.getLongitude());
                routePlanning.putExtra("start_Latitude", lc.getLatitude());
                routePlanning.putExtra("end_Longitude", tude[0]);
                routePlanning.putExtra("end_Latitude", tude[1]);
                startActivity(routePlanning);
                ToastUtils.toast(this, "去这里");
                break;
        }
    }

    /**
     * 百度坐标系(BD-09)转火星坐标系(GCJ-02)
     * <p/>
     * 百度——>谷歌、高德
     *
     * @param bd_lon 百度坐标纬度
     * @param bd_lat 百度坐标经度
     * @return 火星坐标数组
     */
    public static double[] bd09togcj02(double bd_lon, double bd_lat) {
        final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[]{gg_lng, gg_lat};
    }

    private void showPopupWindow(View parent, final float price) {
        final BigDecimal money = new BigDecimal(price);
        final View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_buy_oil, null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.getBackground().setAlpha(150);
        idMoneyText = (TextView) view.findViewById(R.id.id_money_text);
        final TextView idNunText = (TextView) view.findViewById(R.id.id_num_text);

        id1Text = (TextView) view.findViewById(R.id.id_1_text);
        id10Text = (TextView) view.findViewById(R.id.id_10_text);
        id20Text = (TextView) view.findViewById(R.id.id_20_text);
        id50Text = (TextView) view.findViewById(R.id.id_50_text);

        id100Text = (TextView) view.findViewById(R.id.id_100_text);
        id200Text = (TextView) view.findViewById(R.id.id_200_text);
        id500Text = (TextView) view.findViewById(R.id.id_500_text);
        id1000Text = (TextView) view.findViewById(R.id.id_1000_text);


        id1Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPriceItem();
                whichCheck = 1;
                setBackgroundPriceItem(id1Text, true);
                computedPrice(price, whichCheck, Integer.valueOf(idNunText.getText().toString().trim()));
            }
        });

        id10Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPriceItem();
                whichCheck = 2;
                setBackgroundPriceItem(id10Text, true);
                computedPrice(price, whichCheck, Integer.valueOf(idNunText.getText().toString().trim()));
            }
        });
        id20Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPriceItem();
                whichCheck = 3;
                setBackgroundPriceItem(id20Text, true);
                computedPrice(price, whichCheck, Integer.valueOf(idNunText.getText().toString().trim()));
            }
        });
        id50Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPriceItem();
                whichCheck = 4;
                setBackgroundPriceItem(id50Text, true);
                computedPrice(price, whichCheck, Integer.valueOf(idNunText.getText().toString().trim()));
            }
        });
        id100Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPriceItem();
                whichCheck = 5;
                setBackgroundPriceItem(id100Text, true);
                computedPrice(price, whichCheck, Integer.valueOf(idNunText.getText().toString().trim()));
            }
        });
        id200Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPriceItem();
                whichCheck = 6;
                setBackgroundPriceItem(id200Text, true);
                computedPrice(price, whichCheck, Integer.valueOf(idNunText.getText().toString().trim()));
            }
        });
        id500Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPriceItem();
                whichCheck = 7;
                setBackgroundPriceItem(id500Text, true);
                computedPrice(price, whichCheck, Integer.valueOf(idNunText.getText().toString().trim()));
            }
        });
        id1000Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPriceItem();
                whichCheck = 8;
                setBackgroundPriceItem(id1000Text, true);
                computedPrice(price, whichCheck, Integer.valueOf(idNunText.getText().toString().trim()));
            }
        });
        ImageView idPlusImage = (ImageView) view.findViewById(R.id.id_plus_image);
        ImageView idMinusImage = (ImageView) view.findViewById(R.id.id_minus_image);
        idPlusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whichCheck == 0) {
                    ToastUtils.toast(StationActivity.this, "先选择金额");
                } else {
                    int a = Integer.valueOf(idNunText.getText().toString().trim()) + 1;
                    idNunText.setText(a + "");
                    computedPrice(price, whichCheck, a);
                }

            }
        });
        idMinusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.valueOf(idNunText.getText().toString().trim()) - 1;
                idNunText.setText(a + "");
                computedPrice(price, whichCheck, a);
            }
        });

        TextView idBlankText = (TextView) view.findViewById(R.id.id_blank_text);

        Button idAlipayButton = (Button) view.findViewById(R.id.id_alipay_button);
        Button idWeichatpayButton = (Button) view.findViewById(R.id.id_weichatpay_button);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK)
                    popupWindow.dismiss();
                return false;
            }
        });
        idBlankText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        idAlipayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String a = idMoneyText.getText().toString().trim();
                ToastUtils.toast(StationActivity.this, "支付宝支付" + a);
                Order myorder = new Order();
                myorder.setStationName(station.getName());
                myorder.setStationAddress(station.getAddress());
                myorder.setOilType(orderoilname);
                myorder.setTotal(a);
                myorder.setFlag("未完成");
                myorder.setStationPic(station.getStationurls().get(1));
                myorder.setUser(user);
                popupWindow.dismiss();
                Intent intent = new Intent(StationActivity.this, OrderActivity.class);
                intent.putExtra("myorder", myorder);
                startActivity(intent);

            }
        });
        idWeichatpayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = idMoneyText.getText().toString().trim();
                ToastUtils.toast(StationActivity.this, "微信支付" + a);
                Order myorder = new Order();
                myorder.setStationName(station.getName());
                myorder.setStationAddress(station.getAddress());
                myorder.setOilType(orderoilname);
                myorder.setTotal(a);
                myorder.setFlag("未完成");
                myorder.setStationPic(station.getStationurls().get(1));
                myorder.setUser(user);
                popupWindow.dismiss();
                Intent intent = new Intent(StationActivity.this, OrderActivity.class);
                intent.putExtra("myorder", myorder);
                startActivity(intent);
            }
        });
    }

    private void computedPrice(float p, int which, int number) {
        switch (which) {
            case 1:
                BigDecimal num = new BigDecimal(Integer.valueOf(number));
                BigDecimal price = new BigDecimal(p);
                double n = num.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                idMoneyText.setText(n + "");
                break;
            case 2:
                idMoneyText.setText(10 * number + "");
                break;
            case 3:
                idMoneyText.setText(20 * number + "");
                break;
            case 4:
                idMoneyText.setText(50 * number + "");
                break;
            case 5:
                idMoneyText.setText(100 * number + "");
                break;
            case 6:
                idMoneyText.setText(200 * number + "");
                break;
            case 7:
                idMoneyText.setText(500 * number + "");
                break;
            case 8:
                idMoneyText.setText(1000 * number + "");
                break;
            default:
                idMoneyText.setText(0 + "");
                break;
        }
    }

    private void initPriceItem() {
        setBackgroundPriceItem(id1Text, false);
        setBackgroundPriceItem(id10Text, false);
        setBackgroundPriceItem(id20Text, false);
        setBackgroundPriceItem(id50Text, false);
        setBackgroundPriceItem(id100Text, false);
        setBackgroundPriceItem(id200Text, false);
        setBackgroundPriceItem(id500Text, false);
        setBackgroundPriceItem(id1000Text, false);
        idMoneyText.setText(0 + "");
    }

    private void setBackgroundPriceItem(TextView tv, boolean isCheck) {
        if (isCheck) {
            tv.setBackgroundColor(getResources().getColor(R.color.colorPriceCheck));
            tv.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            tv.setBackgroundColor(getResources().getColor(R.color.colorPrice));
            tv.setTextColor(getResources().getColor(R.color.colorTextMain));
        }
    }


    public class StationAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Map> list;
        private StationPriceHolder holder;

        public StationAdapter(ArrayList<Map> M, Context c) {
            this.list = M;
            this.context = c;
        }

        @Override
        public int getCount() {
            Log.i("item数量", list.size() + "");
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView != null) {
                holder = (StationPriceHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.stationpriceitem, null);
                holder = new StationPriceHolder();
                holder.type = (TextView) convertView.findViewById(R.id.id_oil_text_item);
                holder.price = (TextView) convertView.findViewById(R.id.id_price_text_item);
                holder.bookoil = (Button) convertView.findViewById(R.id.id_book_oil_button_item);
                convertView.setTag(holder);

            }
            String name = (String) list.get(position).get("name");
            String price = (String) list.get(position).get("price");
            if (name.equals("0#")) {
                name = name;
            } else name = name + "汽油";
            holder.type.setText(name);
            holder.price.setText("单价：" + price);
//            holder.bookoil.setOnClickListener(new  View.OnClickListener() {
//                @Override
//                public void onClick (View v){
//                    showPopupWindow(idBookOilButton, 6.79f);//第二个参数是油价
//
//                }
//            });
            holder.bookoil.setOnClickListener(new MyOnClickListener(price, name));


            return convertView;
        }

        public class StationPriceHolder {
            public TextView type;

            public TextView price;
            public Button bookoil;
        }

        private class MyOnClickListener implements View.OnClickListener {
            private String price;
            private String type;

            public MyOnClickListener(String price, String type) {
                this.price = price;
                this.type = type;
            }

            @Override
            public void onClick(View v) {
                Log.i("我的监听类", type + price);
                orderoilname = type;
                Float f = Float.parseFloat(price);
                showPopupWindow(idBookOilButton, f);//第二个参数是油价
            }
        }
    }

    //    获得地理位置
    public void getLocationMethod() {
        String locationProvider;
        final int SHOW_LOCATION = 0;
        //获取地理位置管理器
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);

        if (location != null) {
            //不为空,显示地理位置经纬度
lc=location;
            showLocation(location);
        } else {
            Toast.makeText(this, "location为空", Toast.LENGTH_SHORT).show();
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String position = (String) msg.obj;
                    //postionView.setText(position);

                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 显示地理位置经度和纬度信息
     *
     * @param location
     */
    private void showLocation(final Location location) {
        String locationStr = "维度：" + location.getLatitude() +"\n"
				+ "经度：" + location.getLongitude();

    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            showLocation(location);

        }
    };

}
