package com.example.hc.chat.server;

/**
 * Created by hc on 2016/9/6.
 */
import com.example.hc.chat.data.Friend;

import java.util.*;
public class Dog{
    private String id = "w";
    private String result = "w";
    private List<Friend> friends = new ArrayList<>();

    public void setId(String id) {
        this.id = id;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    public List<Friend> getFriends() {
        return friends;
    }
}
