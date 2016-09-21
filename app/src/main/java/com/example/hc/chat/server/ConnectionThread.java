package com.example.hc.chat.server;

import com.example.hc.chat.data.MyMessage;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hc on 2016/8/31.
 */
public class ConnectionThread extends Thread {
    private Socket socket;
    public BufferedReader reader;
    public BufferedWriter writer;
    private List<OnMessageListener> onMessageListenerList;

    public ConnectionThread(Socket socket){
        super();
        this.socket = socket;
        onMessageListenerList = new ArrayList<OnMessageListener>();
    }

    public void connect(){
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
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
            writer.write(data);
        }catch (IOException e){e.printStackTrace();}
    }

    public void sendMessage(String jasonData){
        try {
            writer.write(jasonData);
        }catch (IOException e){e.printStackTrace();}
    }

    @Override
    public void run(){
        while (true){
            try {
                if (reader.ready()) {
                    String data = reader.readLine();
                    Gson gson = new Gson();
                    MyMessage myMessage = gson.fromJson(data, MyMessage.class);
                    if(onMessageListenerList.size() > 0){
                        for(OnMessageListener listener : onMessageListenerList){
                            listener.onReceive(myMessage);
                        }
                    }
                }
            }catch (IOException e){e.printStackTrace();}
        }
    }


    public static interface OnMessageListener{
        public void onReceive(MyMessage myMessage);
    }

    public void addMessageListener(OnMessageListener onMessageListener){
        onMessageListenerList.add(onMessageListener);
    }

    public void removeMessageListener(OnMessageListener onMessageListener){
        onMessageListenerList.remove(onMessageListener);
    }
}