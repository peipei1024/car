package com.js.car.fragment.home;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.bean.Car;
import com.js.car.bean.MyBmobInstallation;
import com.js.car.bean.User;

import com.js.car.maptest.GasStation;
import com.js.car.maptest.Navigation;
import com.js.car.ui.BindCarActivity;
import com.js.car.ui.MaintainActivity;


import com.js.car.ui.QueryIllegalActivity;
import com.js.car.ui.illegalreslut.IllegalReslutActivity;
import com.js.car.ui.stationlist.StationListActivity;
import com.js.car.utils.IntentUtils;
import com.js.car.utils.ToastUtils;
import com.js.car.view.BannerImg;
import com.js.car.view.GridViewForScrollView;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import the.TestMusicActivity;
import the.muisc.MainActivity;


/*
* Class name :HomeFragment
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
public class HomeFragment extends Fragment {
    @Bind(R.id.id_gridview)
    GridViewForScrollView idGridview;
    @Bind(R.id.id_banner)
    BannerImg idBanner;
    @Bind(R.id.id_plus_image)
    ImageView idPlusImage;
    @Bind(R.id.id_car_text)
    TextView idCarText;

    private ArrayList<String> car_list = new ArrayList<>();
    private List<TypeBean> type_list = new ArrayList<>();
    private ArrayList<String> banner_list = new ArrayList<>();
    private HomeItemAdapter adapter;
    private int[] res_image = {R.mipmap.icon_gps, R.mipmap.icon_music, R.mipmap.icon_station, R.mipmap.icon_oil, R.mipmap.icon_illegal, R.mipmap.icon_maintain, R.mipmap.icon_code};
    private String[] str_type = {"导航", "音乐", "加油站", "预约加油", "违章查询", "汽车维护", "车辆信息"};
    private User user;
    private  String userID;
    private String  InstallationId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = BmobUser.getCurrentUser(this.getActivity(), User.class);
        userID=user.getObjectId();

        for (int a = 0; a < res_image.length; a++) {
            TypeBean tb = new TypeBean();
            tb.setRes_type(res_image[a]);
            tb.setStr_type(str_type[a]);
            type_list.add(tb);
        }

//        car_list.add("宝马(白) 京A•15789");
//        car_list.add("宝马(白) 京A•15745");
        banner_list.add("http://ac-unbx3bnf.clouddn.com/d37d514d3745b404.png");
        banner_list.add("http://bmob-cdn-2264.b0.upaiyun.com/2016/06/14/2a2c742440718e8d80d8b747e71e078e.png");
        //banner_list.add("file://android_asset/banner.png");
//        if(user.getCarinf()!=null)
//        Log.i("车辆",user.getCarinf());
        updateInstallation(getActivity());


    }

    @Override
    public void onStart() {
        super.onStart();
        idBanner.startPlay();
    }

    @Override
    public void onPause() {
        super.onDestroy();
        idBanner.stopPlay();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);

        String carinf = "";
        if (user == null || user.getCarinf() == null||"".equals(user.getCarinf())) {
            carinf = "暂无车辆";
        } else {
            carinf = user.getCarinf();
        }
        idCarText.setText(carinf);
        adapter = new HomeItemAdapter(getActivity(), type_list);
        idGridview.setAdapter(adapter);
        idBanner.setImageUris(banner_list);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnItemClick(R.id.id_gridview)
    public void item(int position) {
        switch (position) {
            case 0://导航
                IntentUtils.doIntent(getActivity(), Navigation.class);
                break;
            case 1://音乐
                IntentUtils.doIntent(getActivity(), MainActivity.class);
                break;
            case 2://加油站
String s=user.getCarinf();
                if(user.getCarinf().equals("")){
                    ToastUtils.toast(getActivity(),"您还没有绑定汽车");
                }else {
                IntentUtils.doIntent(getActivity(), GasStation.class);}
                break;
            case 3://预约加油
                if(user.getCarinf().equals("")){
                    ToastUtils.toast(getActivity(),"您还没有绑定汽车");
                }else {
                IntentUtils.doIntent(getActivity(), StationListActivity.class);}
                break;
            case 4:
                //违章查询
                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_illegal_item);
                TextView idMyCarView = (TextView) window.findViewById(R.id.id_my_car_text);
                TextView idOtherCarView = (TextView) window.findViewById(R.id.id_other_car_text);
                idMyCarView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //传入当前车牌号
                        if(user.getCarinf().equals("")){
                            ToastUtils.toast(getActivity(),"您还没有绑定汽车");
                        }else {
                        IntentUtils.doIntentWithString(getActivity(), IllegalReslutActivity.class,"carinf",user.getCarinf());

                             }
                    }
                });
                idOtherCarView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        IntentUtils.doIntent(getActivity(), QueryIllegalActivity.class);
                    }
                });
                break;
            case 5://故障维护
                if(user.getCarinf().equals("")){
                    ToastUtils.toast(getActivity(),"您还没有绑定汽车");
                }else {
                IntentUtils.doIntentWithString(getActivity(), MaintainActivity.class, "flag", "0");}
                break;
            case 6://汽车信息
//                IntentUtils.doIntent(getActivity(), BindCarActivity.class);
                if(user.getCarinf().equals("")){
                    ToastUtils.toast(getActivity(),"您还没有绑定汽车");
                }else {
                IntentUtils.doIntentWithString(getActivity(), BindCarActivity.class, "flag", "0");}
                break;
        }
        //ToastUtils.toast(getActivity(), str_type[position]);
    }

    @OnClick(R.id.id_plus_image)
    public void onClick() {
        IntentUtils.doIntentWithString(getActivity(), BindCarActivity.class, "flag", "1"+InstallationId);

    }

    private void showPopupWindow(View parent) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_car, null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        ListView listView = (ListView) view.findViewById(R.id.id_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.view_pop_car, car_list);
        listView.setAdapter(adapter);


        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.TOP, 0, 200);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK)
                    popupWindow.dismiss();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.toast(getActivity(), car_list.get(position));
                idCarText.setText(car_list.get(position));
                popupWindow.dismiss();
                user.setCarinf(car_list.get(position));
                user.update(getActivity(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("提示：", "更新个人信息成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtils.toast(getActivity(), s);
                    }
                });
                car_list.clear();
            }
        });

    }


    @OnClick(R.id.id_car_text)
    public void car() {
        Car car = new Car();
        BmobQuery<Car> query = new BmobQuery<Car>();
        query.addWhereEqualTo("user", user);
        car_list.clear();
        query.findObjects(this.getActivity(), new FindListener<Car>() {
            @Override
            public void onSuccess(List<Car> list) {
                if (list.size() == 0) {
                    car_list.add("暂无车辆信息");
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        String carinf = list.get(i).getCarBrand() + "(" + list.get(i).getCarNum() + ")";
                        car_list.add(carinf);
                    }
                }
                showPopupWindow(idCarText);
            }

            @Override
            public void onError(int i, String s) {
                Log.i("error", s);
            }
        });


    }

    /**
     * 用户id与设备号绑定
     * @param context
     */
   public void updateInstallation(final Context context){
       BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
       query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(context));
       query.findObjects(context, new FindListener<MyBmobInstallation>() {

           @Override
           public void onSuccess(List<MyBmobInstallation> object) {
               // TODO Auto-generated method stub
               if(object.size() > 0){
                   MyBmobInstallation mbi = object.get(0);
               InstallationId=    mbi.getInstallationId();
                   mbi.setUid(userID);
                   mbi.update(context,new UpdateListener() {

                       @Override
                       public void onSuccess() {
                           // TODO Auto-generated method stub
                           Log.i("bmob","设备信息更新成功");
                       }

                       @Override
                       public void onFailure(int code, String msg) {
                           // TODO Auto-generated method stub
                           Log.i("bmob","设备信息更新失败:"+msg);
                       }
                   });
               }else{
               }
           }

           @Override
           public void onError(int code, String msg) {
               // TODO Auto-generated method stub
           }
       });


   }
}
