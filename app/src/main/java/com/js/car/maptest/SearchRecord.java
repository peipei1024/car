package com.js.car.maptest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.js.car.R;

import java.util.List;

/**
 * Created by JiaM on 2016/5/22.
 */
public class SearchRecord extends BaseAdapter {

    private Context context;
    private List<RecordUtil> list;

    public SearchRecord(Context context, List<RecordUtil> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    public List<RecordUtil> getList() {
        return list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.navi_search_record_item, parent, false);
            viewHolder.searchRecord = (TextView) convertView.findViewById(R.id.search_record);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.searchRecord.setText(list.get(position).getStartName() + "-" + list.get(position).getEndname());
        return convertView;
    }

    protected class ViewHolder {
        private TextView searchRecord;
    }
}
