package com.js.car.ui.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.js.car.R;
import com.js.car.bean.CarFault;
import com.js.car.maptest.RecordUtil;
import com.js.car.ui.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 景贝贝 on 2016/6/15.
 */
public class MyRecordAadapter extends BaseAdapter{
    private Context context;
    private List<RecordUtil> list;

    public MyRecordAadapter(Context context,List<RecordUtil> list) {
        this.context = context;
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
        recorHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_record_item, null);
            holder = new recorHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (recorHolder) convertView.getTag();
        }
        holder.idTimeText.setText(list.get(position).getData());
        holder.id_car_num_text.setText(list.get(position).getStartName());
        holder. id_action_text.setText(list.get(position).getEndname());

        return convertView;
    }

    static class recorHolder {
        @Bind(R.id.mytime)
        TextView idTimeText;
        @Bind(R.id.mystart)
        TextView id_car_num_text;
        @Bind(R.id.myend)
        TextView id_action_text;


        recorHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
