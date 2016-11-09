package com.js.car;

import com.js.car.bean.Station;

import java.util.ArrayList;

/*
* Class name :Test
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
public class Test {
    public static ArrayList<Station> test(){
        ArrayList<String> url = new ArrayList<>();
        url.add("http://ac-unbx3bnf.clouddn.com/ad9934437b4d4e1c.jpg");
        url.add("http://ac-unbx3bnf.clouddn.com/47aaafbabaf9c19d.jpg");

        Station station = new Station();
        station.setId("1");
        station.setName("太原新康加油站");
        station.setAddress("康西公路与新兰路交叉路口南150米");
        station.setStarts(2);
        station.setStationurls(url);

        Station station1 = new Station();
        station1.setId("2");
        station1.setName("加油站(龙聚东)");
        station1.setAddress("康西公路(近滨河东路)");
        station1.setStarts(0);
        station1.setStationurls(url);

        ArrayList<Station> list = new ArrayList<>();
        list.add(station);
        list.add(station1);
        list.add(station1);
        list.add(station1);
        list.add(station1);
        list.add(station1);
        list.add(station1);
        return list;
    }
}
