package com.js.car.bean;

import cn.bmob.v3.BmobObject;

/*
* Class name :Order
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
public class Order extends BmobObject {
    private String StationName;//车站名
    private String StationAddress;//地址
    private String OilType;//油
    private String Total;//总价
    private User user;//所属用户，一对多关系
    private String flag;//是否完成的标志
    private String CoderMessage;//二维码信息
    private String StationPic;//加油站图片

    public String getCoderMessage() {
        return CoderMessage;
    }

    public void setCoderMessage(String coderMessage) {
        CoderMessage = coderMessage;
    }

    public String getStationPic() {
        return StationPic;
    }

    public void setStationPic(String stationPic) {
        StationPic = stationPic;
    }

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public String getStationAddress() {
        return StationAddress;
    }

    public void setStationAddress(String stationAddress) {
        StationAddress = stationAddress;
    }

    public String getOilType() {
        return OilType;
    }

    public void setOilType(String oilType) {
        OilType = oilType;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
