package com.example.hc.chat.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hc on 2016/9/3.
 */
public class Info {
    private String id;
    private String result;
    private List<Friend> friends = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}
