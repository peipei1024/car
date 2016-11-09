package com.js.car.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.bean.CarFault;
import com.js.car.bean.IllegalInf;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 景贝贝 on 2016/6/15.
 */
public class MyMaintainAdapter extends BaseAdapter{
    private Context context;
    private List<CarFault> list;

    public MyMaintainAdapter(Context context,List<CarFault> list) {
        this.context = context;
        this.list=list;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        carFaultHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_maintain_item, null);
            holder = new carFaultHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (carFaultHolder) convertView.getTag();
        }
        holder.idTimeText.setText(list.get(position).getCreatedAt());
        holder.id_car_num_text.setText(list.get(position).getCarNum());
        holder. id_action_text.setText(list.get(position).getCarMessage());

        return convertView;
    }


    static class carFaultHolder {
        @Bind(R.id.id_time_text)
        TextView idTimeText;
        @Bind(R.id.id_car_num_text)
        TextView id_car_num_text;
        @Bind(R.id.id_action_text)
        TextView id_action_text;


        carFaultHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
