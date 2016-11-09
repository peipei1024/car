package com.js.car;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/*
* Class name :BaseFragmentAdapter
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016/3/3.
*
*/
public class FragmentAdapter extends FragmentPagerAdapter{
    private Context mContext;
    private List<android.support.v4.app.Fragment> mList;
    private ArrayList<String> mPageName;
    public FragmentAdapter(Context context, ArrayList<String> pageName, List<android.support.v4.app.Fragment> list, FragmentManager fm) {
        super(fm);
        mContext = context;
        mList = list;
        mPageName = pageName;
    }



    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageName.get(position);
    }
}
