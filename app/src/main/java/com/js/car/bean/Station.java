package com.js.car.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/*
* Class name :Station
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
public class Station implements Serializable {
    private String id;
    private String name;
    private String address;
    private int starts;//星级
    private ArrayList<String> stationurls;//应该是图片
    private ArrayList<Map> gastprice;//加油站油价
    private String distance;//距离


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public ArrayList<Map> getGastprice() {
        return gastprice;
    }

    public void setGastprice(ArrayList<Map> gastprice) {
        this.gastprice = gastprice;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    private String lon;//百度经度
    private String lat;//百度纬度

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public int getStarts() {
        return starts;
    }

    public void setStarts(int starts) {
        this.starts = starts;
    }

    public ArrayList<String> getStationurls() {
        return stationurls;
    }

    public void setStationurls(ArrayList<String> stationurls) {
        this.stationurls = stationurls;
    }
}
