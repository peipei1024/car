package com.js.car.fragment.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.bean.Order;
import com.js.car.bean.Station;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by 景贝贝 on 2016/5/21.
 */
public class MyOrderAdapter extends BaseAdapter{
    private Context context;
    private  ArrayList olist;
    public MyOrderAdapter(Context context, ArrayList olist) {
    this.context=context;
        this.olist=olist;
    }

    @Override
    public int getCount() {
        return olist.size();
    }

    @Override
    public Object getItem(int position) {
        return olist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderItemHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.view_order_item, null);
            holder = new OrderItemHolder();

            holder.orderpic = (ImageView) convertView.findViewById(R.id.id_pic_image);
            holder.orderstationname = (TextView) convertView.findViewById(R.id.id_order_station_name);
            holder.orderflag = (TextView) convertView.findViewById(R.id.id_order_flag);
            holder.ordertotal = (TextView) convertView.findViewById(R.id.id_order_money);
            holder.orderoil = (TextView) convertView.findViewById(R.id.id_order_oiltype);
            holder.ordertime = (TextView) convertView.findViewById(R.id.id_order_time);
            convertView.setTag(holder);
        }else {
            holder = (OrderItemHolder) convertView.getTag();
        }

        Order order = (Order) olist.get(position);
        if (order.getStationPic().length() >= 0){
            Picasso.with(context).load(order.getStationPic()).into(holder.orderpic);
        }
        if (order.getStationName() != null){
            holder.orderstationname.setText(order.getStationName());
        }

        if (order.getFlag() != null){
            holder.orderflag.setText(order.getFlag());
        }
        if (order.getTotal() != null){
            holder.ordertotal.setText(order.getTotal());
        }
        if (order.getOilType() != null){
            holder.orderoil.setText(order.getOilType());
        }
        if (order.getCreatedAt() != null){
            holder.ordertime.setText(order.getCreatedAt());
        }
        return convertView;
    }

    public class OrderItemHolder{
        TextView orderstationname;
        TextView orderflag;
        TextView ordertotal;
        TextView orderoil;
        TextView ordertime;
        ImageView orderpic;

    }
}
