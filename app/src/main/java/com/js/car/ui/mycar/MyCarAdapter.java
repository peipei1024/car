package com.js.car.ui.mycar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.bean.Car;


import java.util.ArrayList;

/*
* Class name :MyCarAdapter
*
* Version information :
*
* Describe ：
*
* Author ：景贝贝
*
* Created by pei on 2016-5-7.
*
*/
public class MyCarAdapter extends BaseAdapter{
    ArrayList<Car> carlist=null;
    Context context;

    public MyCarAdapter(ArrayList<Car> carlist,Context c) {
        this.carlist=carlist;
        this.context=c;
    }

    @Override
    public int getCount() {
        return carlist.size();
    }

    @Override
    public Object getItem(int position) {
        return carlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       CarHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.view_car, null);
            holder = new CarHolder();
            holder.carnum = (TextView) convertView.findViewById(R.id.id_car_num_text);
            holder.carbrand = (TextView) convertView.findViewById(R.id.id_car_brand_text);
            holder.carmodel = (TextView) convertView.findViewById(R.id.id_car_model_text);
            holder.carcolor = (TextView) convertView.findViewById(R.id.id_car_color_text);
            holder.cartype = (TextView) convertView.findViewById(R.id.id_car_type_text);
            convertView.setTag(holder);
        }else {
            holder = (CarHolder) convertView.getTag();
        }

        Car car = carlist.get(position);
        if (car.getCarNum() != null){
            holder.carnum.setText(car.getCarNum());
        }
        if (car.getCarBrand() != null){
            holder.carbrand.setText(car.getCarBrand());
        }
        if (car.getCarModle() != null){
            holder.carmodel.setText(car.getCarModle());
        }
        if (car.getCarColor() != null){
            holder.carcolor.setText(car.getCarColor());
        }
        if (car.getCarType() != null){
            holder.cartype.setText(car.getCarType());
        }


        return convertView;
    }
    public  class  CarHolder{
        TextView carnum;
        TextView carbrand;
        TextView carmodel;
        TextView carcolor;
        TextView cartype;

    }

}
