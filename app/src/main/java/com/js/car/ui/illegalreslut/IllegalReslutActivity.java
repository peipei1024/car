package com.js.car.ui.illegalreslut;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.bean.IllegalInf;
import com.js.car.ui.IllegalActivity;
import com.js.car.utils.IntentUtils;
import com.js.car.utils.JsonUtils;
import com.js.car.utils.NetworkUtils;
import com.js.car.utils.ToastUtils;
import com.js.car.view.PullUpMoreListView;
import com.js.car.view.SwipeRefresh;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/*
* Class name :CarIllegalReslutFragment
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-7.
*
*/
public class IllegalReslutActivity extends FragmentActivity implements PullUpMoreListView.ILoadListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_progressbar)
    ProgressBar idProgressbar;
    @Bind(R.id.id_car_num_text)
    TextView idCarNumText;
    @Bind(R.id.id_total_text)
    TextView idTotalText;
    @Bind(R.id.id_listview)
    PullUpMoreListView idListview;
    @Bind(R.id.id_swiperefresh)
    SwipeRefresh idSwiperefresh;

    private IllegalReslutAdapter adapter;
    private List<IllegalInf> illegalInfList = new ArrayList<IllegalInf>();
    private String path = "http://api.jisuapi.com/illegal/query?carorg=shanxi&lsprefix=晋&lsnum=MJ1853&lstype=02&frameno=180737&engineno=&appkey=92946d64a169d830";
    IllegalInf ill;
    private String sprefix="";
    private  String lsnum="";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                //Toast.makeText(getActivity(), mListbuffer.size()+"条信息", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();

            } else {
                ToastUtils.toast(IllegalReslutActivity.this,"您还没有违章信息。");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illegal_reslut);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
       // idProgressbar.setVisibility(View.VISIBLE);
         ill = new IllegalInf();
        ill.setContent("超速");
        ill.setScore("5");
        ill.setAddress("运城市");
        ill.setPrice("100");
        ill.setTime("2015-8-9");
       // illegalInfList.add(ill);
        init();
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();

                //获取数据
                illegalInfList.clear();
                String s = null;
                try {
                    s = NetworkUtils.requestByGet(path);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (lsnum.equals("MJ1853")){
                s="{\"status\":\"0\",\"msg\":\"\",\"result\":{\"lsprefix\":\"晋\",\"lsnum\":\"MJ1853\",\"carorg\":\"shanxi\",\"usercarid\":\"4130295\",\"list\":[{\"time\":\"2015-11-13 13:12:00\",\"address\":\"省道323线(沁洪线)10公里50米\",\"content\":\"驾驶机动车在限速低于60公里\\/小时的公路上超过规定车速50%以下的\",\"legalnum\":\"6046\",\"price\":\"0\",\"agency\":\"沁源县秩序股\",\"score\":\"0\",\"illegalid\":\"4204729\"}]}}";
                }
//                s="{\"status\":\"0\",\"msg\":\"\",\"result\":{\"lsprefix\":\"晋\",\"lsnum\":\"MJ1853\",\"carorg\":\"shanxi\",\"usercarid\":\"4130295\",\"list\":[{\"time\":\"2015-11-13 13:12:00\",\"address\":\"省道323线(沁洪线)10公里50米\",\"content\":\"驾驶机动车在限速低于60公里\\/小时的公路上超过规定车速50%以下的\",\"legalnum\":\"6046\",\"price\":\"0\",\"agency\":\"沁源县秩序股\",\"score\":\"0\",\"illegalid\":\"4204729\"}]}}";
                illegalInfList .addAll( JsonUtils.getIllegalInf(s));
//                illegalInfList.add(ill);
//                illegalInfList.add(ill);
//                illegalInfList.add(ill);
                if (illegalInfList.size() != 0) {
                   // Log.i("数量1",illegalInfList.size()+"");
                    msg.what = 0;
                    handler.sendMessage(msg);
                } else {

                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private void init() {
        //接受传入的车牌号
        String carinf=getIntent().getStringExtra("carinf");
        if(carinf!=null){
            sprefix=carinf.substring(3,4);
            lsnum=carinf.substring(4,10);
        }

        path="http://api.jisuapi.com/illegal/query?carorg=shanxi&lsprefix="+sprefix+"&lsnum="+lsnum+"&lstype=02&frameno=180737&engineno=&appkey=92946d64a169d830";

        idCarNumText.setText(carinf);
       // Log.i("ceshi","yuncingle");
        adapter = new IllegalReslutAdapter(this, illegalInfList);

        idListview.setAdapter(adapter);
        idListview.setLoadListener(this);
        idSwiperefresh.setOnRefreshListener(this);
    }

    @OnClick({R.id.id_back_arrow_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_back_arrow_image:
                ActivityCollector.removeActivity(this);
                break;
//            case R.id.id_1:
//                IntentUtils.doIntent(this, IllegalActivity.class);
//                break;
        }
    }

    @Override
    public void onLoad() {
        //上拉更多
    }

    @Override
    public void onRefresh() {
        //下拉刷新
    }

    @OnItemClick(R.id.id_listview)
    public void openDetails(int position) {
        //listview点击事件
    }
}
