package com.js.car.utils;

import com.js.car.bean.IllegalInf;
import com.js.car.bean.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by 景贝贝 on 2016/5/12.
 */
public class JsonUtils {
    /**
     * 返回违章信息集合
     *
     * @param s json字符串
     * @return
     */
    public static List<IllegalInf> getIllegalInf(String s) {
        List<IllegalInf> illegelinflist = new ArrayList<IllegalInf>();
        try {
            JSONObject jb = new JSONObject(s);
            JSONObject result = jb.getJSONObject("result");
            JSONArray ja = result.getJSONArray("list");
            for (int i = 0; i < ja.length(); i++) {
                IllegalInf inf = new IllegalInf();
                JSONObject jbb = ja.getJSONObject(i);
                inf.setTime(jbb.getString("time"));
                inf.setAddress(jbb.getString("address"));
                inf.setContent(jbb.getString("content"));
                inf.setPrice(jbb.getString("price"));
                inf.setScore(jbb.getString("score"));
                illegelinflist.add(inf);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return illegelinflist;

    }

    /**
     * 获得地区加油站信息
     * @param s
     * @return
     */
    public static List<Station> getJsonRegionStation(String s) {
        List<Station> list = new ArrayList<Station>();
        try {
            JSONObject jb = new JSONObject(s);
            JSONObject result = jb.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");
            Random random=new Random();
            for (int i = 0; i < data.length(); i++) {
                Station station = new Station();
                ArrayList<String> url = new ArrayList<>();
                url.add("http://ac-unbx3bnf.clouddn.com/ad9934437b4d4e1c.jpg");
                url.add("http://ac-unbx3bnf.clouddn.com/47aaafbabaf9c19d.jpg");
                JSONObject stationob = data.getJSONObject(i);
                String name = stationob.getString("name");
                String address = stationob.getString("address");
                String type = stationob.getString("type");
                String lon = stationob.getString("lon");
                String lat = stationob.getString("lat");

                JSONArray price = stationob.getJSONArray("gastprice");

                ArrayList<Map> pricelist=new ArrayList<Map>();
                for (int j = 0; j < price.length(); j++) {
                    Map<String, String> map = new HashMap<String, String>();
                    JSONObject priceob = price.getJSONObject(j);
                    String oilname = priceob.getString("name");
                    String oilprice = priceob.getString("price");
                    map.put("price", oilprice);
                    map.put("name",oilname);
                    pricelist.add(map);
                }
                int r=random.nextInt(5) ;
                station.setStarts(r);
                station.setName(name);
                station.setAddress(address);
                station.setDistance(type);
                station.setLon(lon);
                station.setLat(lat);
                station.setStationurls(url);
                station.setGastprice(pricelist);
                list.add(station);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public  static Station getJsonOneStation(String s){
        List<Station> list = new ArrayList<Station>();
        try {
            JSONObject jb = new JSONObject(s);
            JSONObject result = jb.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");
            Random random=new Random();
            for (int i = 0; i < data.length(); i++) {
                Station station = new Station();
                ArrayList<String> url = new ArrayList<>();
                url.add("http://ac-unbx3bnf.clouddn.com/ad9934437b4d4e1c.jpg");
                url.add("http://ac-unbx3bnf.clouddn.com/47aaafbabaf9c19d.jpg");
                JSONObject stationob = data.getJSONObject(i);
                String name = stationob.getString("name");
                String address = stationob.getString("address");
                String type = stationob.getString("type");
                String lon = stationob.getString("lon");
                String lat = stationob.getString("lat");
                JSONArray price = stationob.getJSONArray("gastprice");

                ArrayList<Map> pricelist=new ArrayList<Map>();
                for (int j = 0; j < price.length(); j++) {
                    Map<String, String> map = new HashMap<String, String>();
                    JSONObject priceob = price.getJSONObject(j);
                    String oilname = priceob.getString("name");
                    String oilprice = priceob.getString("price");
                    map.put("price", oilprice);
                    map.put("name",oilname);
                    pricelist.add(map);
                }
                int r=random.nextInt(5) ;
                station.setStarts(r);
                station.setName(name);
                station.setAddress(address);
                station.setDistance(type);
                station.setLon(lon);
                station.setLat(lat);
                station.setStationurls(url);
                station.setGastprice(pricelist);
                list.add(station);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list.get(0);
    }

}
