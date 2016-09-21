package com.example.hc.chat.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hc on 2016/8/31.
 */
public class User{
    private String id;
    private String type;
    HashMap<String, String> map = new HashMap<>();

    public void setId(String id) {

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            }
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return super.toString()+",---id ="+id+",type ="+type;
    }
}
