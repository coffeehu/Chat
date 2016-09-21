package com.example.hc.chat.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by hc on 2016/9/5.
 */
public class HttpUtil {
    //封装的发送请求函数

    /**
     *
     * @param address
     * @param id
     * @param password  可以为空
     * @param type   有四种：login, register, select, watch ,皆为 String
     *               当为 watch 的时候，id 为 "我"的 id，password 为要关注的用户的 id
     * @param listener
     */
    public static void sendHttpRequest(final String address,final String id, final String password, final String type, final HttpCallbackListener listener) {
        if (!HttpUtil.isNetworkAvailable()){
            //这里写相应的网络设置处理
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                String result = null;
                InputStreamReader in = null;
                OutputStream out = null;

                try{
                    URL url = new URL(address);
                    //使用HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    //设置方法和参数
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(4000);
                    connection.setReadTimeout(4000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Charset", "utf-8");
                    String data = null;

                    if(type.equals("login")||type.equals("register")) {
                        data = "id=" + URLEncoder.encode(id, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&type=" + URLEncoder.encode(type, "UTF-8");//传递的数据
                    }else if(type.equals("select")){
                        data = "id=" + URLEncoder.encode(id, "UTF-8") +   "&type=" + URLEncoder.encode(type, "UTF-8");
                    }else if(type.equals("watch")){
                        data = "myId=" + URLEncoder.encode(id, "UTF-8") + "&hisId=" + URLEncoder.encode(password, "UTF-8") + "&type=" + URLEncoder.encode(type, "UTF-8");//传递的数据
                    }
                    connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    connection.connect();

                   out = connection.getOutputStream();
                   out.write(data.getBytes());
                   out.flush();

                    in = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(in);

                    result = bufferedReader.readLine();//接收从服务器返回的数据
                    in.close();
                    out.close();
                    if(result!=null&&result.length()>0){
                        listener.onSuccess(result);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFail(e);
                    //出现异常则回调onError
                }finally {
                    connection.disconnect();
                }

            }
        }).start();
    }



    //判断当前网络是否可用
    public static boolean isNetworkAvailable(){
        //这里检查网络，后续再添加
        return true;
    }
}
