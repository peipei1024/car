package com.js.car.ui.station;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.a.a.a.This;
import com.js.car.R;
import com.js.car.bean.Station;
import com.js.car.bean.Word;

import java.util.ArrayList;
import java.util.Map;

/*
* Class name :StationAdapter
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-6.
*
*/
class StationAdapter1 extends BaseAdapter {
    private Context context;
    private ArrayList<Map> list;
    private StationPriceHolder holder;

    public StationAdapter1(ArrayList<Map> M, Context c) {
        this.list = M;
        this.context = c;
    }

    @Override
    public int getCount() {
        Log.i("item数量", list.size() + "");
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
        if (convertView != null) {
            holder = (StationPriceHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.stationpriceitem, null);
            holder = new StationPriceHolder();
            holder.type = (TextView) convertView.findViewById(R.id.id_oil_text_item);
            holder.price = (TextView) convertView.findViewById(R.id.id_price_text_item);
            holder.bookoil = (Button) convertView.findViewById(R.id.id_book_oil_button_item);
            convertView.setTag(holder);

        }
        String name = (String) list.get(position).get("name");
        String price = (String) list.get(position).get("price");
        if (name.length() > 3) {
            name = name+"";
        } else{
            name = name + "汽油";
        }
        holder.type.setText(name);
        holder.price.setText("单价：" + price);


        return convertView;
    }

    public class StationPriceHolder {
        public TextView type;

        public TextView price;
        public Button bookoil;
    }
}
