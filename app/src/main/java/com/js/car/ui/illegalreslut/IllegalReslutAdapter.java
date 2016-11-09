package com.js.car.ui.illegalreslut;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.bean.IllegalInf;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
* Class name :IllegalReslutAdapter
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-7.
*
*/
public class IllegalReslutAdapter extends BaseAdapter {
    private Context context;
    private List<IllegalInf> list;

    public IllegalReslutAdapter(Context c,List<IllegalInf> list) {
        this.context = c;
        this.list=list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IllegalHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_illegal_item, null);
            holder = new IllegalHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (IllegalHolder) convertView.getTag();
        }
        holder.idTimeText.setText(list.get(position).getTime());
        holder.idPlaceText.setText(list.get(position).getAddress());
        holder.idActionText.setText(list.get(position).getContent());
        holder.idMoneyandfenText.setText("罚款"+list.get(position).getPrice()+"，"+list.get(position).getScore()+"分");

//        holder.idTimeText.setText("");
//        holder.idActionText.setText("");
//        holder.idMoneyandfenText.setText("");
//        holder.idPlaceText.setText("");
//        holder.idStateText.setText("");

        return convertView;
    }



    static class IllegalHolder {
        @Bind(R.id.id_time_text)
        TextView idTimeText;
        @Bind(R.id.id_moneyandfen_text)
        TextView idMoneyandfenText;
        @Bind(R.id.id_state_text)
        TextView idStateText;
        @Bind(R.id.id_place_text)
        TextView idPlaceText;
        @Bind(R.id.id_action_text)
        TextView idActionText;

        IllegalHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
