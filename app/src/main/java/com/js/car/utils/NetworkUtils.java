package com.js.car.utils;


import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 景贝贝 on 2016/5/12.
 */
public class NetworkUtils {
    // Get方式请求
    public static String requestByGet(String path ) throws Exception {
       // String path = "10.128.7.34:3000/name=helloworld&password=android";
        String result;
        // 新建一个URL对象
        URL url = new URL(path);
        // 打开一个HttpURLConnection连接
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        // 设置连接超时时间
        urlConn.setConnectTimeout(6 * 1000);
        // 开始连接
        urlConn.connect();
        // 判断请求是否成功
        if (urlConn.getResponseCode() == 200) {
            // 获取返回的数据
            byte[] data = readStream(urlConn.getInputStream());
            Log.i("tag_get", new String(data, "UTF-8"));
            result= new String(data, "UTF-8");
        } else {
            result="失败";
            Log.i("tag_get", "Get方式请求失败");
        }
        // 关闭连接
        urlConn.disconnect();
        return result;
    }
    /**
     * 读取流
     *
     * @param inStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }
}
