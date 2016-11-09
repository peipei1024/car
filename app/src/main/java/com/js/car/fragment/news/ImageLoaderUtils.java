package com.js.car.fragment.news;

import android.content.Context;
import android.widget.ImageView;

import com.js.car.R;
import com.squareup.picasso.Picasso;

/**
 * Description : 图片加载工具类
 * Author : lauren
 * Email  : lauren.liuling@gmail.com
 * Blog   : http://www.liuling123.com
 * Date   : 15/12/21
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if(imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
//        Glide.with(context).load(url).placeholder(placeholder)
//                .error(error).crossFade().into(imageView);
        Picasso.with(context).load(url).placeholder(placeholder).error(error).noFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url) {
        if(imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
//        Glide.with(context).load(url).placeholder(R.drawable.ic_image_loading)
//                .error(R.drawable.ic_image_loadfail).crossFade().into(imageView);
        Picasso.with(context).load(url).placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_image_loadfail).into(imageView);
    }


}
