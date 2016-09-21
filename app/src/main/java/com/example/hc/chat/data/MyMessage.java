package com.example.hc.chat.data;

import java.io.Serializable;

/**
 * Created by hc on 2016/8/30.
 */
public class MyMessage implements Serializable{
    public String id;
    public String destId;
    public String content;
    public String bitmapString = null;
    public String type = "message"; // 此 type 用于服务器来判断接收的数据的类型
    private String localType = "me";// me/frd  用于判断是我发的还是对方发的


    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocalType(String localType) {
        this.localType = localType;
    }

    public void setBitmapString(String bitmapString) {
        this.bitmapString = bitmapString;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getDestId() {
        return destId;
    }

    public String getType() {
        return type;
    }

    public String getLocalType() {
        return localType;
    }

    public String getBitmapString() {
        return bitmapString;
    }
}
