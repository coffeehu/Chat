package com.example.hc.chat.util;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.hc.chat.R;
import com.example.hc.chat.data.MyMessage;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hc on 2016/8/31.
 */
public class ConnectionThread extends Thread {
    private  String ip = "123.56.15.171";
    private  int port = 55555;
    private Socket socket;
    private BufferedReader reader;
    //private BufferedWriter writer;
    private OutputStream writer;
    private List<OnMessageListener> onMessageListenerList;
    private boolean isConnect = false;

    public ConnectionThread(String ip, int port){
        super();
        this.ip = ip;
        this.port = port;
        onMessageListenerList = new ArrayList<OnMessageListener>();
        Log.d("chattest","connectionthread construtor");
    }

    public void connect(){

                try {
                    isConnect = true;
                    socket = new Socket(ip, port);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
                    //writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
                    writer = socket.getOutputStream();
                    Log.d("chattest","connectionThread connect()");
                    start();
                }catch (IOException e){e.printStackTrace();}

    }

    /**
     * 此方法必须确认已经执行了 connect 后才能调用
     * @param myMessage
     */
    public void sendMessage(MyMessage myMessage){
        try {
            Gson gson = new Gson();
            String data = gson.toJson(myMessage);
            //writer.write(data+"\r\n");
            writer.write((data+"\n").getBytes());
            Log.d("chattest","connectionThread sendMessage(MyMessage),data ="+data+",writer ="+writer.toString());
        }catch (IOException e){e.printStackTrace();}
    }

    public void sendMessage(String jasonData){
        try {
            //writer.write(jasonData);
            Log.d("Logintest", "connectthread sendmesssage  writern = "+writer.toString());//---------------------------
            writer.write((jasonData+ "\n").getBytes());
            Log.d("Logintest", "connectthread sendmesssage 222222222  writer ="+writer.toString());//---------------------------
            Log.d("chattest","connectionThread sendMessage(string),jsondata ="+jasonData+",writer ="+writer.toString());
        }catch (IOException e){e.printStackTrace();}
    }



    @Override
    public void run(){
        while (isConnect){
            try {
                if (reader.ready()) {
                    Log.d("chattest","connectionThread connect() run() ready()");
                    String data = reader.readLine();
                    Log.d("chattest","connectionThread connect() run() ready(), data = "+data+",listenerlist.size = "+onMessageListenerList.size());
                    //----------------Gson gson = new Gson();
                    //----------------MyMessage myMessage = gson.fromJson(data, MyMessage.class);
                    if(onMessageListenerList.size() > 0){
                        //-----------------onMessageListenerList.get(0).onReceive(myMessage);
                        for(OnMessageListener listener : onMessageListenerList) {
                            listener.onReceive(data);
                        }
                    }
                }
            }catch (IOException e){e.printStackTrace();}
        }
    }


    public interface OnMessageListener{
        void onReceive(String data);//------------------
    }

    public void addMessageListener(OnMessageListener onMessageListener){
        Log.d("chattest","connectionThread addlistener()");
        onMessageListenerList.add(onMessageListener);
    }

    public void removeMessageListener(OnMessageListener onMessageListener){
        onMessageListenerList.remove(onMessageListener);
    }

    public void clearMessageListener(){
        onMessageListenerList.clear();
    }

    public void disconnect() throws IOException{
        isConnect = false;
        reader.close();
        writer.close();
        socket.close();
    }
}
