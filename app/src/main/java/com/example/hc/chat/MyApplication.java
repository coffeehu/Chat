package com.example.hc.chat;

import android.app.Application;

import com.example.hc.chat.data.Friend;
import com.example.hc.chat.data.MyMessage;
import com.example.hc.chat.util.ConnectionThread;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hc on 2016/8/31.
 */
public class MyApplication extends Application {
    private String id;
    private ConnectionThread connectionThread;
    private List<Friend> friends;
    private List<Friend> watchList;
    private List<Friend> fansList;
    private HashMap<String, List<MyMessage>> messagesMap = new HashMap<>();
    private int softKeyboardHeight;

    public void setId(String id) {
        this.id = id;
    }

    public void setConnectionThread(ConnectionThread connectionThread) {
        this.connectionThread = connectionThread;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public void setFansList(List<Friend> fansList) {
        this.fansList = fansList;
    }

    public void setWatchList(List<Friend> watchList) {
        this.watchList = watchList;
    }

    public void setMessagesMap(HashMap<String, List<MyMessage>> messagesMap) {
        this.messagesMap = messagesMap;
    }

    public void setSoftKeyboardHeight(int softKeyboardHeight) {
        this.softKeyboardHeight = softKeyboardHeight;
    }

    public String getId() {
        return id;
    }

    public ConnectionThread getConnectionThread() {
        return connectionThread;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public List<Friend> getFansList() {
        return fansList;
    }

    public List<Friend> getWatchList() {
        return watchList;
    }

    public HashMap<String, List<MyMessage>> getMessagesMap() {
        return messagesMap;
    }

    public int getSoftKeyboardHeight() {
        return softKeyboardHeight;
    }
}
