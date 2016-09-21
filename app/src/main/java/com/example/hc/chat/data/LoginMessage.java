package com.example.hc.chat.data;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by hc on 2016/8/31.
 */
public class LoginMessage {
    private String id;
    private String type;
    private String password;

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return super.toString()+",---id ="+id+",type ="+type+",password ="+password;
    }
}
