package com.js.car.utils;/*
* Class name :LogUtils
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016/3/17.
*
*/

import android.util.Log;

public class LogUtils {
    private static final String TAG = "ToolLog";

    /**
     * 上线后关闭log
     */
    private static final Boolean DEBUG = true;

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(TAG + ":" + tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if (DEBUG) {
            Log.e(TAG + ":" + tag, msg);
        }
    }
    public static void d(String tag, String msg){
        if (DEBUG) {
            Log.e(TAG + ":" + tag, msg);
        }
    }
    public static void w(String tag, String msg){
        if (DEBUG) {
            Log.e(TAG + ":" + tag, msg);
        }
    }
}
