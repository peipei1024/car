package com.js.car.ui.mycar;

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
import com.js.car.ui.EditCarActivity;
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

/**
 * Created by 景贝贝 on 2016/5/21.
 */
public class EditMyCarActivity extends Activity {
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
    private String result;
    private User user;
    private String InstallationId;
    private String MYmessage = "";
    private String carId = "";
    private Car CurrentCar = null;
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
            }

            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_car);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        idSaveView.setVisibility(View.GONE);
        idScanCodeView.setVisibility(View.GONE);

        //String flag = getIntent().getStringExtra("flag");
        CurrentCar= (Car) getIntent().getSerializableExtra("car");
        init();

    }
    public void init(){
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
    }

    @OnClick({R.id.id_back_arrow_image, R.id.id_save_view, R.id.id_scan_code_view, R.id.id_edit_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_back_arrow_image:
                ActivityCollector.removeActivity(this);
                break;

            case R.id.id_edit_view:
                ToastUtils.toast(this, "编辑");
                Intent eintent = new Intent(EditMyCarActivity.this, EditCarActivity.class);
                if (result != null && result.split("，").length == 13) {
                    eintent.putExtra("information", result);
                    startActivity(eintent);
                } else {
                    ToastUtils.toast(this, "请先扫描二维码");
                }

                break;
        }
    }




}
