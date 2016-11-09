package com.js.car.maptest;

/**
 * Created by JiaM on 2016/5/22.
 */
public class RecordUtil {
    private String startName;
    private String endname;
    private double start_Longitude;
    private double start_Latitude;
    private double end_Longitude;
    private double end_Latitude;
    private String data;

    public String getData() {
        return data;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getEndname() {
        return endname;
    }

    public void setEndname(String endname) {
        this.endname = endname;
    }

    public double getStart_Longitude() {
        return start_Longitude;
    }

    public void setStart_Longitude(double start_Longitude) {
        this.start_Longitude = start_Longitude;
    }

    public double getStart_Latitude() {
        return start_Latitude;
    }

    public void setStart_Latitude(double start_Latitude) {
        this.start_Latitude = start_Latitude;
    }

    public double getEnd_Longitude() {
        return end_Longitude;
    }

    public void setEnd_Longitude(double end_Longitude) {
        this.end_Longitude = end_Longitude;
    }

    public double getEnd_Latitude() {
        return end_Latitude;
    }

    public void setEnd_Latitude(double end_Latitude) {
        this.end_Latitude = end_Latitude;
    }

    public void setData(String data) {
        this.data = data;
    }
}
