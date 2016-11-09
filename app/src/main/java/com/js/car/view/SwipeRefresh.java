package com.js.car.view;/*
* Class name :SwipeRefresh
*
* Version information :
*
* Describe ：重写SwipeRefreshLayout，为解决首次进入自动刷新问题，将swipeRefresh.setRefreshing(true)即可调用刷新动画
*
* Author ：裴徐泽
*
* Created by pei on 2016/3/17.
*
*/

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class SwipeRefresh extends SwipeRefreshLayout {
    private boolean mMeasured = false;
    private boolean mRefresh = false;
    public SwipeRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeRefresh(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mMeasured){
            mMeasured = true;
            setRefreshing(mRefresh);
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        if (mMeasured){
            super.setRefreshing(refreshing);
        }else {
            mRefresh = refreshing;
        }
    }
}
