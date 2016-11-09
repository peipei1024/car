package com.js.car.fragment.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.js.car.R;
import com.js.car.bean.Car;
import com.js.car.bean.Order;
import com.js.car.bean.User;
import com.js.car.ui.OrderActivity;
import com.js.car.ui.mycar.MyCarAdapter;
import com.js.car.utils.IntentUtils;
import com.js.car.utils.ToastUtils;
import com.js.car.view.PullUpMoreListView;
import com.js.car.view.SwipeRefresh;
import com.zxing.view.ViewfinderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/*
* Class name :DoingOrderFragment
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-8.
*
*/
public class DoingOrderFragment extends Fragment{

    private ArrayList<Order> orderlist=new ArrayList<Order>();
    private User user;
    MyOrderAdapter orderAdapter=null;



    private PullUpMoreListView idOrderListview;

    private SwipeRefresh idOrderSwiperefresh;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                orderAdapter.notifyDataSetChanged();
                idOrderSwiperefresh.setRefreshing(false);
            } else {

                idOrderSwiperefresh.setRefreshing(false);
                ToastUtils.toast(getActivity(), "暂无订单信息");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doing_order, null);
        idOrderListview= (PullUpMoreListView) view.findViewById(R.id.id_order_listview);
        idOrderSwiperefresh= (SwipeRefresh) view.findViewById(R.id.id_order_swiperefresh);
        idOrderSwiperefresh.setRefreshing(true);
        user = BmobUser.getCurrentUser(this.getActivity(), User.class);
        orderlist.clear();
        getOrderList();
        orderAdapter=new MyOrderAdapter(getActivity(),orderlist);
        idOrderListview.setAdapter(orderAdapter);
        idOrderListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(getActivity(), EditOrderActivity.class);
                intent.putExtra("myorder",orderlist.get(position));
                startActivity(intent);
            }
        });
        return view;
    }
    public void getOrderList(){
        BmobQuery<Order> query = new BmobQuery<Order>();

        query.addWhereEqualTo("user", user);    // 查询当前用户的所有帖子
        query.addWhereEqualTo("flag", "未完成");


        query.findObjects(getActivity(), new FindListener<Order>() {
            @Override
            public void onSuccess(List<Order> object) {
                // TODO Auto-generated method stub
                //carlist = (ArrayList) object;
                orderlist.addAll((ArrayList<Order>)object);

                Message mes = new Message();
                mes.what = 1;
                handler.sendMessage(mes);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.toast(getActivity(), msg);
                Log.i("订单错误信息",msg);
            }
        });

    }

}
