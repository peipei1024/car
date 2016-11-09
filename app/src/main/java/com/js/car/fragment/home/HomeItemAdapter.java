package com.js.car.fragment.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.js.car.R;

import java.util.ArrayList;
import java.util.List;

/*
* Class name :HomeGoodAdapter
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-4-16.
*
*/
public class HomeItemAdapter extends BaseAdapter{
    private List<TypeBean> list = new ArrayList<>();
    private Context context;
    public HomeItemAdapter(Context context, List<TypeBean> l){
        this.list = l;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TypeHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.view_type, null);
            holder = new TypeHolder();
            holder.view_type = (LinearLayout) convertView.findViewById(R.id.id_view_type);
            holder.image_type = (ImageView) convertView.findViewById(R.id.id_image);
            holder.text_type = (TextView) convertView.findViewById(R.id.id_text);
            convertView.setTag(holder);
        }else {
            holder = (TypeHolder) convertView.getTag();
        }

        TypeBean type = list.get(position);
        //设置图片holder.image_pic
        holder.image_type.setImageResource(type.getRes_type());
        holder.text_type.setText(type.getStr_type());

        return convertView;
    }
    class TypeHolder{
        public LinearLayout view_type;
        public ImageView image_type;
        public TextView text_type;
    }
}
