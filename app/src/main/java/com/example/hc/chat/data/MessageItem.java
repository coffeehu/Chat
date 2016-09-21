package com.example.hc.chat.data;

/**
 * Created by hc on 2016/9/11.
 */
public class MessageItem {
    private String frdId;
    private int number;
    private String latestContent;
    private String time;

    public void setFrdId(String frdId) {
        this.frdId = frdId;
    }

    public void setLatestContent(String latestContent) {
        this.latestContent = latestContent;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumber() {
        return number;
    }

    public String getFrdId() {
        return frdId;
    }

    public String getLatestContent() {
        return latestContent;
    }

    public String getTime() {
        return time;
    }
}
