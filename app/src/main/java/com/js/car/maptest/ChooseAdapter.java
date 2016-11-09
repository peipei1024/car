package com.js.car.maptest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.js.car.R;

import java.util.List;

/**
 * Created by JiaM on 2016/5/11.
 */
public class ChooseAdapter extends BaseAdapter {

    private Context context;
    private List<PoiItem> data;

    public ChooseAdapter(Context context, List<PoiItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.route_inputs, null);
            viewHolder.location_title = (TextView) convertView.findViewById(R.id.location_title);
            viewHolder.location_content = (TextView) convertView.findViewById(R.id.location_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.location_title.setText(data.get(position).getTitle());
        viewHolder.location_content.setText(data.get(position).getAdName() + "." + data.get(position).getSnippet());
        return convertView;
    }

    protected class ViewHolder {
        private TextView location_title;
        private TextView location_content;
    }
}
