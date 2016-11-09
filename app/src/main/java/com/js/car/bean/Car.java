package com.js.car.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 景贝贝 on 2016/5/9.
 */
public class Car extends BmobObject {


    private User user;//所属用户，一对多关系
    private String carNum;//车牌号
    private String carBrand;//品牌
    private String carModle;//型号
    private String carColor;//颜色
    private String carType;//车身级别
    private String carEngine;//发动机号
    private String carLamp;//车灯
    private String carOil;//汽油量
    private String carMileage;//总里程数
    private String carEngineCheck;//发动机性能
    private String carVariatorCheck;//变速器性能
    private String carNewMileage;//上次维护后里程数
    private String carFrameNo;//车架号

    public String getCarFrameNo() {
        return carFrameNo;
    }

    public void setCarFrameNo(String carFrameNo) {
        this.carFrameNo = carFrameNo;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCarNewMileage() {
        return carNewMileage;
    }

    public void setCarNewMileage(String carNewMileage) {
        this.carNewMileage = carNewMileage;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModle() {
        return carModle;
    }

    public void setCarModle(String carModle) {
        this.carModle = carModle;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarEngine() {
        return carEngine;
    }

    public void setCarEngine(String carEngine) {
        this.carEngine = carEngine;
    }

    public String getCarLamp() {
        return carLamp;
    }

    public void setCarLamp(String carLamp) {
        this.carLamp = carLamp;
    }

    public String getCarOil() {
        return carOil;
    }

    public void setCarOil(String carOil) {
        this.carOil = carOil;
    }

    public String getCarMileage() {
        return carMileage;
    }

    public void setCarMileage(String carMileage) {
        this.carMileage = carMileage;
    }

    public String getCarEngineCheck() {
        return carEngineCheck;
    }

    public void setCarEngineCheck(String carEngineCheck) {
        this.carEngineCheck = carEngineCheck;
    }

    public String getCarVariatorCheck() {
        return carVariatorCheck;
    }

    public void setCarVariatorCheck(String carVariatorCheck) {
        this.carVariatorCheck = carVariatorCheck;
    }
}
