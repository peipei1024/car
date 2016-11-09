package com.js.car.utils;

import android.content.Context;
import android.widget.Toast;

/*
* Class name :ToastUtils
*
* Version information :1.0.0
*
* Describe ：toast工具类
*
* Author ：peixuze
*
* Created by pei on 2016/2/8.
*
*/
public class ToastUtils {
    public static void toast(Context context, String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}

