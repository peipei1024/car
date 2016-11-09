package com.js.car.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.bean.User;
import com.js.car.ui.AccountActivity;
import com.js.car.ui.BindCarActivity;
import com.js.car.ui.MyMaintainActivity;
import com.js.car.ui.SettingActivity;
import com.js.car.ui.illegalreslut.IllegalReslutActivity;
import com.js.car.ui.mycar.MyCarActivity;
import com.js.car.ui.myillegal.MyIllegalActivity;
import com.js.car.ui.myorder.MyOrderActivity;
import com.js.car.ui.record.MyRecordActivity;
import com.js.car.utils.IntentUtils;
import com.js.car.utils.ToastUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/*
* Class name :MyFragment
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-3.
*
*/
public class MyFragment extends Fragment {
    @Bind(R.id.id_message_image)
    ImageView idMessageImage;
    @Bind(R.id.id_setting_image)
    ImageView idSettingImage;
    @Bind(R.id.id_head_image)
    ImageView idHeadImage;
    @Bind(R.id.id_name_text)
    TextView idNameText;
    @Bind(R.id.id_phone_text)
    TextView idPhoneText;
    @Bind(R.id.id_account_view)
    LinearLayout idAccountView;
    @Bind(R.id.id_car_view)
    RelativeLayout idCarView;
    @Bind(R.id.id_order_view)
    RelativeLayout idOrderView;
    @Bind(R.id.id_record_view)
    RelativeLayout idRecordView;
    @Bind(R.id.id_illegal_view)
    RelativeLayout idIllegalView;
    @Bind(R.id.id_maintain_view)
    RelativeLayout idMaintainView;
    @Bind(R.id.id_bindcar_view)
    RelativeLayout idBindcarView;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, view);
        user = BmobUser.getCurrentUser(getActivity(), User.class);
        init();
        return view;
    }

    private void init() {
        if (user.getPicpath()!=null) {
            Picasso.with(getActivity()).load(user.getPicpath()).into(idHeadImage);
        }
        if (user.getNickname()!= null) {
            idNameText.setText(user.getNickname());
        } else {
            idNameText.setText(user.getUsername());
        }
        idPhoneText.setText(user.getMobilePhoneNumber());


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.id_message_image, R.id.id_setting_image, R.id.id_account_view, R.id.id_car_view, R.id.id_order_view, R.id.id_record_view, R.id.id_illegal_view, R.id.id_maintain_view, R.id.id_bindcar_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_message_image:

                break;
            case R.id.id_setting_image:
                IntentUtils.doIntent(getActivity(), SettingActivity.class);
                break;
            case R.id.id_account_view:
                IntentUtils.doIntent(getActivity(), AccountActivity.class);
                break;
            case R.id.id_car_view:
                IntentUtils.doIntent(getActivity(), MyCarActivity.class);
                break;
            case R.id.id_order_view:
                IntentUtils.doIntent(getActivity(), MyOrderActivity.class);
                break;
            case R.id.id_record_view:
                IntentUtils.doIntent(getActivity(), MyRecordActivity.class);
                break;
            case R.id.id_illegal_view:
                if(user.getCarinf().equals("暂无车辆")){
                    ToastUtils.toast(getActivity(),"您还没有绑定汽车");
                }else {
                    user = BmobUser.getCurrentUser(getActivity(), User.class);
                    IntentUtils.doIntentWithString(getActivity(), IllegalReslutActivity.class,"carinf",user.getCarinf());

                }
               // IntentUtils.doIntent(getActivity(), MyIllegalActivity.class);
                break;
            case R.id.id_maintain_view://维护记录
                IntentUtils.doIntent(getActivity(), MyMaintainActivity.class);
                break;
            case R.id.id_bindcar_view://绑定汽车
                IntentUtils.doIntentWithString(getActivity(), BindCarActivity.class, "flag", "1");
                break;
        }
    }
}
