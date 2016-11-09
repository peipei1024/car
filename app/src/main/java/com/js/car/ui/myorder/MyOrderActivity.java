package com.js.car.ui.myorder;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.js.car.FragmentAdapter;
import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.fragment.order.AllOrderFragment;
import com.js.car.fragment.order.CompleteOrderFragment;
import com.js.car.fragment.order.DoingOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* Class name :MyOrderActivity
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
public class MyOrderActivity extends FragmentActivity {
    @Bind(R.id.id_back_arrow_image)
    ImageView idBackArrowImage;
    @Bind(R.id.id_tablayout)
    TabLayout idTablayout;
    @Bind(R.id.id_viewpager)
    ViewPager idViewpager;

    private FragmentAdapter adapter;
    private ArrayList<String> pageName = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        pageName.add("待加油");
        pageName.add("已加油");
        pageName.add("全部");

        CompleteOrderFragment completeOrderFragment = new CompleteOrderFragment();
        DoingOrderFragment doingOrderFragment = new DoingOrderFragment();
        AllOrderFragment allOrderFragment = new AllOrderFragment();
        fragmentList.add(doingOrderFragment);
        fragmentList.add(completeOrderFragment);
        fragmentList.add(allOrderFragment);

        adapter = new FragmentAdapter(this, pageName, fragmentList, getSupportFragmentManager());
        idViewpager.setAdapter(adapter);
        idTablayout.setupWithViewPager(idViewpager);
    }
    @OnClick(R.id.id_back_arrow_image)
    public void onClick() {
        ActivityCollector.removeActivity(this);
    }
}
