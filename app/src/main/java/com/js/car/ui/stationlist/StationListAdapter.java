package com.js.car.ui.stationlist;

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
import com.js.car.bean.Station;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
* Class name :StationListAdapter
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-4.
*
*/
public class StationListAdapter extends BaseAdapter{
    private ArrayList<Station> list = null;
    private Context context;
    public StationListAdapter(ArrayList<Station> l, Context c){
        this.context = c;
        this.list = l;
    }
    public void refreshDta(ArrayList<Station> l){
        this.list = l;
        notifyDataSetChanged();
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
        StationHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.view_station_item, null);
            holder = new StationHolder();
            holder.station_view = (LinearLayout) convertView.findViewById(R.id.id_station_view);
            holder.pic_image = (ImageView) convertView.findViewById(R.id.id_pic_image);
            holder.name_text = (TextView) convertView.findViewById(R.id.id_name_text);
            holder.starts_ratingbar = (RatingBar) convertView.findViewById(R.id.id_starts_ratingbar);
            holder.address_text = (TextView) convertView.findViewById(R.id.id_address_text);
            holder.distance_text = (TextView) convertView.findViewById(R.id.id_distance_text);
            convertView.setTag(holder);
        }else {
            holder = (StationHolder) convertView.getTag();
        }

        Station station = list.get(position);
        if (station.getStationurls().size() >= 0){
            Picasso.with(context).load(station.getStationurls().get(0)).into(holder.pic_image);
        }
        if (station.getName() != null){
            holder.name_text.setText(station.getName());
        }
        if (station.getStarts() >= 0){
            holder.starts_ratingbar.setRating(station.getStarts());
        }else {
            holder.starts_ratingbar.setRating(0);
        }
        if (station.getAddress() != null){
            holder.address_text.setText(station.getAddress());
        }
        holder.distance_text.setText("200米");
        return convertView;
    }

    public class StationHolder{
        public LinearLayout station_view;
        public ImageView pic_image;
        public TextView name_text;
        public RatingBar starts_ratingbar;
        public TextView address_text;
        public TextView distance_text;
    }
}
