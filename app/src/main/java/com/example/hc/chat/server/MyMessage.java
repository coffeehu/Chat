package com.example.hc.chat.server;

/**
 * Created by hc on 2016/8/30.
 */
public class MyMessage {
    public String id;
    public String destId;
    public String content;
    public String type = "message";


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
}
