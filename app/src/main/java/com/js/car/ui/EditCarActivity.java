package com.js.car.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.Car;
import com.js.car.bean.CarFault;
import com.js.car.bean.User;
import com.js.car.utils.ToastUtils;

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

/**
 * Created by 景贝贝 on 2016/5/8.
 */
public class EditCarActivity extends Activity {

    @Bind(R.id.id_lamp_edit)
    EditText idLampEdit;
    @Bind(R.id.id_oil_edit)
    EditText idOilEdit;
    @Bind(R.id.id_mileage_edit)
    EditText idMileageEdit;
    @Bind(R.id.id_engine_check_edit)
    EditText idEngineCheckEdit;
    @Bind(R.id.id_variator_check_edit)
    EditText idVariatorCheckEdit;
    @Bind(R.id.id_new_mileage_edit)
    EditText idNewMileageEdit;
    @Bind(R.id.id_edit_save_view)
    LinearLayout idSaveView;
   private List<String> CarImfor;
    private User user;
    private String carId="";
    private  String InstallationId;
    private String MYmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        String result = getIntent().getStringExtra("information");
        InstallationId=getIntent().getStringExtra("InstallationId");

         CarImfor = new ArrayList<>();
        CarImfor = Arrays.asList(result.split("，"));
        idLampEdit.setText(CarImfor.get(7));
        idOilEdit.setText(CarImfor.get(8));
        idMileageEdit.setText(CarImfor.get(9));
        idEngineCheckEdit.setText(CarImfor.get(10));
        idVariatorCheckEdit.setText(CarImfor.get(11));
        idNewMileageEdit.setText(CarImfor.get(12));
        user= BmobUser.getCurrentUser(this,User.class);
        String CurrentCarNum = user.getCarinf().substring(3, 10);
        queryCarId(CurrentCarNum);
    }

    @OnClick({R.id.id_edit_save_view, R.id.id_edit_back_arrow_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_edit_back_arrow_image:
                ActivityCollector.removeActivity(this);
                break;
            case R.id.id_edit_save_view:
                if(user==null){
                    ToastUtils.toast(this, "您还没有登录");
                    break;
                }
                String s1 = idLampEdit.getText().toString();
                String s2 = idOilEdit.getText().toString();
                String s3 = idMileageEdit.getText().toString();
                String s4 = idEngineCheckEdit.getText().toString();
                String s5 = idVariatorCheckEdit.getText().toString();
                String s6 = idNewMileageEdit.getText().toString();
                if (Integer.parseInt(s2.substring(0,s2.length()-1) )< 20) {
                    MYmessage = "您的油量已低于20%。";
                }
                if (Integer.parseInt(s3.substring(0,s3.length()-2)) > 15000) {
                    MYmessage = MYmessage + "您已行驶" + s3 + "KM,请注意维护。";
                }
                if (s5.equals("异常")) {
                    MYmessage = MYmessage + "变速器异常，请注意维修。";
                }
                if (s4.equals("异常")) {
                    MYmessage = MYmessage + "发动机异常，请注意维修。";
                }
                Car car=new Car();
                car.setUser(user);
                car.setCarNum(CarImfor.get(0));
                car.setCarBrand(CarImfor.get(1));
                car.setCarModle(CarImfor.get(2));
                car.setCarColor(CarImfor.get(3));
                car.setCarType(CarImfor.get(4));
                car.setCarEngine(CarImfor.get(5));
                car.setCarLamp(s1);
                car.setCarOil(s2);
                car.setCarMileage(s3);
                car.setCarEngineCheck(s4);
                car.setCarVariatorCheck(s5);
                car.setCarNewMileage(s6);
                final CarFault carfault=new CarFault();
                carfault.setUser(user);
                carfault.setCarNum(CarImfor.get(0));
                carfault.setCarMessage(MYmessage);

                if(carId.equals("fals"))
                    car.save(this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.toast(EditCarActivity.this, "保存成功");
                            myPushMessage();
                            saveCarfault(carfault);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            ToastUtils.toast(EditCarActivity.this, "保存失败");
                        }
                    });
                car.update(this, carId, new UpdateListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        ToastUtils.toast(EditCarActivity.this, "保存成功");
                        myPushMessage();
                        saveCarfault(carfault);
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        ToastUtils.toast(EditCarActivity.this, "保存失败g");
                    }
                });


                break;

        }
    }

    public void queryCarId(String carnum){
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
                    carId=car.getObjectId();
                    Log.i("汽车id",carId);

                }
            }
            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                System.out.print("bindactivity 查询失败"+msg);
                carId="false";
            }
        });



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
    public void saveCarfault( CarFault mycarfault){
        mycarfault.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.i("维护记录","success");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
