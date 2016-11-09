package com.js.car.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/*
* Class name :User
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
public class User extends BmobUser{
   private String mypassword;//密码
    private String nickname;//昵称
    private String sex;//性别
    private String picpath;//头像路径
    private String carinf;//当前车辆号

    public String getCarinf() {
        return carinf;
    }

    public void setCarinf(String carinf) {
        this.carinf = carinf;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getMypassword() {
        return mypassword;
    }

    public void setMypassword(String mypassword) {
        this.mypassword = mypassword;
    }



}
