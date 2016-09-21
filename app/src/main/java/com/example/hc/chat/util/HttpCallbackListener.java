package com.example.hc.chat.util;

/**
 * Created by hc on 2016/9/5.
 */
public interface HttpCallbackListener {

    void onSuccess(String response);

    void onFail(Exception e);

}
