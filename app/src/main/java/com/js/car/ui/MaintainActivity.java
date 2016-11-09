package com.js.car.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.Car;
import com.js.car.bean.User;
import com.js.car.utils.ToastUtils;
import com.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/*
* Class name :MaintainActivity
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
public class MaintainActivity extends Activity{

    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_car_num_text)
    TextView idCarNumText;
    @Bind(R.id.id_car_brand_text)
    TextView idCarBrandText;
    @Bind(R.id.id_car_model_text)
    TextView idCarModelText;
    @Bind(R.id.id_car_color_text)
    TextView idCarColorText;
    @Bind(R.id.id_car_type_text)
    TextView idCarTypeText;
    @Bind(R.id.id_engine_text)
    TextView idEngineText;
    @Bind(R.id.id_lamp_text)
    TextView idLampText;
    @Bind(R.id.id_oil_text)
    TextView idOilText;
    @Bind(R.id.id_mileage_text)
    TextView idMileageText;
    @Bind(R.id.id_engine_check_text)
    TextView idEngineCheckText;
    @Bind(R.id.id_variator_check_text)
    TextView idVariatorCheckText;
    @Bind(R.id.id_new_mileage_text)
    TextView idNewMileageText;
    @Bind(R.id.id_frameno_text)
    TextView idFramenoText;
    @Bind(R.id.id_save_view)
    LinearLayout idSaveView;
    @Bind(R.id.id_scan_code_view)
    LinearLayout idScanCodeView;
    @Bind(R.id.id_edit_view)
    LinearLayout idEditView;
    TextView guzhangtv;
    TextView fenshutv;

    private String result;
    private User user;
    private String InstallationId;
    private String MYmessage = "";
    private String carId = "";
    private Car CurrentCar = null;
    private TextView carinfotv;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                idCarNumText.setText(CurrentCar.getCarNum());
                idCarBrandText.setText(CurrentCar.getCarBrand());
                idCarModelText.setText(CurrentCar.getCarModle());
                idCarColorText.setText(CurrentCar.getCarColor());
                idCarTypeText.setText(CurrentCar.getCarType());
                idEngineText.setText(CurrentCar.getCarEngine());
                idFramenoText.setText(CurrentCar.getCarFrameNo());
                idLampText.setText(CurrentCar.getCarLamp());
                idOilText.setText(CurrentCar.getCarOil());
                idMileageText.setText(CurrentCar.getCarMileage());
                idEngineCheckText.setText(CurrentCar.getCarEngineCheck());
                idVariatorCheckText.setText(CurrentCar.getCarVariatorCheck());
                idNewMileageText.setText(CurrentCar.getCarNewMileage());
                result = CurrentCar.getCarNum() + "，" + CurrentCar.getCarBrand() + "，" + CurrentCar.getCarModle() + "，" + CurrentCar.getCarColor() + "，" + CurrentCar.getCarType()
                        + "，" + CurrentCar.getCarEngine() + "，" + CurrentCar.getCarFrameNo() + "，" + CurrentCar.getCarLamp() + "，"
                        + CurrentCar.getCarOil() + "，" + CurrentCar.getCarMileage() + "，" + CurrentCar.getCarEngineCheck() + "，" + CurrentCar.getCarVariatorCheck() +
                        "，" + CurrentCar.getCarNewMileage();
                Log.i("result结果", result);
                int j=0;
                if(CurrentCar.getCarLamp().indexOf("坏")>=0){
                    j=j+1;
                }
                if(CurrentCar.getCarEngineCheck().indexOf("异常")>=0){
                    j=j+1;
                }
                if(CurrentCar.getCarVariatorCheck().indexOf("异常")>=0){
                    j=j+1;
                }
                if( Integer.parseInt(CurrentCar.getCarOil().substring(0,CurrentCar.getCarOil().length()-1))<20){
                    j=j+1;
                }
                if( Integer.parseInt(CurrentCar.getCarMileage().substring(0,CurrentCar.getCarOil().length()-2))>15000){
                    j=j+1;
                }
                guzhangtv.setText(j+"个故障");
                fenshutv.setText((5-j)*20+"分");


            }

            super.handleMessage(msg);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
LinearLayout car_base_info_layout= (LinearLayout) findViewById(R.id.car_base_info_layout);
        car_base_info_layout.setVisibility(View.GONE);
        idLampText.setFocusable(false);
        idLampText.setEnabled(false);
        carinfotv= (TextView) findViewById(R.id.car_info_tv);
        guzhangtv= (TextView) findViewById(R.id.guzhangtv);
        fenshutv= (TextView) findViewById(R.id.fenshutv);

        user = BmobUser.getCurrentUser(this, User.class);
        String flag = getIntent().getStringExtra("flag");
        if (flag.length() > 1) {
            InstallationId = flag.substring(1);
            Log.i("设备id", InstallationId);
            flag = flag.substring(0, 1);
        }
        if (flag.equals("0")) {
            idSaveView.setVisibility(View.GONE);
            idScanCodeView.setVisibility(View.GONE);

            String CurrentCarNum = user.getCarinf().substring(3, 10);
            Log.i("bindcar当前车辆", CurrentCarNum);
            queryCurrentCarId(CurrentCarNum);
            // Log.i("dangqianchepai",CurrentCar.getCarNum());
//
        }


    }

    @OnClick({R.id.id_back_arrow_image, R.id.id_save_view, R.id.id_scan_code_view, R.id.id_edit_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_back_arrow_image:
                ActivityCollector.removeActivity(this);
                break;
            case R.id.id_save_view:
                if (user == null) {
                    ToastUtils.toast(this, "您还没有登录");
                    break;
                }
                Car car = new Car();
                car.setUser(user);
                car.setCarNum(idCarNumText.getText().toString());
                car.setCarBrand(idCarBrandText.getText().toString());
                car.setCarModle(idCarModelText.getText().toString());
                car.setCarColor(idCarColorText.getText().toString());
                car.setCarType(idCarTypeText.getText().toString());
                car.setCarEngine(idEngineText.getText().toString());
                car.setCarFrameNo(idFramenoText.getText().toString());
                car.setCarLamp(idLampText.getText().toString());
                car.setCarOil(idOilText.getText().toString());
                car.setCarMileage(idMileageText.getText().toString());
                car.setCarEngineCheck(idEngineCheckText.getText().toString());
                car.setCarVariatorCheck(idVariatorCheckText.getText().toString());
                car.setCarNewMileage(idNewMileageText.getText().toString());

                if (carId.equals("fals"))
                    car.save(this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.toast(MaintainActivity.this, "保存成功");
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            ToastUtils.toast(MaintainActivity.this, "保存失败");
                        }
                    });
                car.update(this, carId, new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        ToastUtils.toast(MaintainActivity.this, "保存成功");
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        ToastUtils.toast(MaintainActivity.this, "保存失败g");
                    }
                });

                break;
            case R.id.id_scan_code_view:
                //ToastUtils.toast(this, "扫描二维码");
                Intent startScan = new Intent(MaintainActivity.this, CaptureActivity.class);
//				startActivity(startScan);
                startActivityForResult(startScan, 0);
                break;
            case R.id.id_edit_view:
                ToastUtils.toast(this, "编辑");
                Intent eintent = new Intent(MaintainActivity.this, EditCarActivity.class);
                if (result != null && result.split("，").length == 13) {
                    eintent.putExtra("information", result);
                    startActivity(eintent);
                } else {
                    ToastUtils.toast(this, "请先扫描二维码");
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            result = data.getExtras().getString("result");
            ToastUtils.toast(this, result);
            List<String> CarImfor = new ArrayList<>();
            if (result != null && result.split("，").length == 13) {
                CarImfor = Arrays.asList(result.split("，"));
//                Log.d("size",CarImfor.size()+"");
//                Log.d("0",CarImfor.get(0)+"dd");
                idCarNumText.setText(CarImfor.get(0));
                idCarBrandText.setText(CarImfor.get(1));
                idCarModelText.setText(CarImfor.get(2));
                idCarColorText.setText(CarImfor.get(3));
                idCarTypeText.setText(CarImfor.get(4));
                idEngineText.setText(CarImfor.get(5));
                idFramenoText.setText(CarImfor.get(6));
                idLampText.setText(CarImfor.get(7));
                idOilText.setText(CarImfor.get(8));
                idMileageText.setText(CarImfor.get(9));
                idEngineCheckText.setText(CarImfor.get(10));
                idVariatorCheckText.setText(CarImfor.get(11));
                idNewMileageText.setText(CarImfor.get(12));
                int Oil = Integer.parseInt(CarImfor.get(8).substring(0, (CarImfor.get(8).length() - 1)));
                int NewMileage = Integer.parseInt(CarImfor.get(12).substring(0, (CarImfor.get(12).length() - 2)));
                if (Oil < 20) {
                    MYmessage = "您的油量已低于20%。";
                }
                if (NewMileage > 15000) {
                    MYmessage = MYmessage + "您已行驶" + NewMileage + "KM,请注意维护。";
                }
                if (CarImfor.get(11).equals("异常")) {
                    MYmessage = MYmessage + "变速器异常，请注意维修。";
                }
                if (CarImfor.get(10).equals("异常")) {
                    MYmessage = MYmessage + "发动机异常，请注意维修。";
                }
                myPushMessage();
                queryCarId(CarImfor.get(0));
            } else {
                ToastUtils.toast(this, "扫描失败，请重新扫描");
            }


        }
    }

    /**
     * 推送消息
     */
    public void myPushMessage() {
        String myinstallationId = InstallationId;
        BmobPushManager bmobPush = new BmobPushManager(this);
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("installationId", myinstallationId);
        bmobPush.setQuery(query);
        bmobPush.pushMessage(MYmessage);

    }

    /**
     * 返回车牌
     *
     * @param carnum
     */
    public void queryCarId(String carnum) {
        BmobQuery<Car> query = new BmobQuery<Car>();
//查询playerName叫“比目”的数据
        query.addWhereEqualTo("carNum", carnum);
//返回50条数据，如果不加上这条语句，默认返回10条数据
        //query.setLimit(50);
//执行查询方法
        query.findObjects(this, new FindListener<Car>() {
            @Override
            public void onSuccess(List<Car> object) {
                // TODO Auto-generated method stub

                for (Car car : object) {

                    //获得数据的objectId信息
                    carId = car.getObjectId();
                    Log.i("汽车id", carId);

                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                System.out.print("bindactivity 查询失败" + msg);
                carId = "false";
            }
        });


    }

    /**
     * 返回绑定的车辆信息
     *
     * @param carnum
     */
    public void queryCurrentCarId(String carnum) {
        BmobQuery<Car> query = new BmobQuery<Car>();
//查询playerName叫“比目”的数据
        query.addWhereEqualTo("carNum", carnum);
//返回50条数据，如果不加上这条语句，默认返回10条数据
        //query.setLimit(50);
//执行查询方法
        query.findObjects(this, new FindListener<Car>() {
            @Override
            public void onSuccess(List<Car> object) {
                // TODO Auto-generated method stub

                for (Car car : object) {

                    //获得数据的objectId信息
                    CurrentCar = car;
                    Log.i("当前车牌", CurrentCar.getCarNum());

                }
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                System.out.print("bindactivity 查询失败" + msg);
                carId = "false";
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
        });


    }


}
