package com.js.car.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 景贝贝 on 2016/6/15.
 */
public class CarFault extends BmobObject{
    private User user;//所属用户，一对多关系
    private String carNum;//车牌号
    private String carMessage;//维护信息
    public String getCarMessage() {
        return carMessage;
    }

    public void setCarMessage(String carMessage) {
        this.carMessage = carMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }





}
