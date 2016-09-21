package com.example.hc.chat.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.hc.chat.MyApplication;
import com.example.hc.chat.R;
import com.example.hc.chat.data.Friend;
import com.example.hc.chat.data.Info;
import com.example.hc.chat.fragment.FriendFansFragment;
import com.example.hc.chat.fragment.FriendWatchFragment;
import com.example.hc.chat.util.HttpCallbackListener;
import com.example.hc.chat.util.HttpUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by hc on 2016/9/8.
 */
public class UserActivity extends AppCompatActivity {
    private TextView userName;
    private Button watch;
    private String hisId;
    private List<Friend> fansList;
    private List<Friend> watchList;
    private String myId;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        hisId = intent.getStringExtra("userName");  //对方的 id
        initView();
    }

    private void initView(){
        myApplication = (MyApplication)getApplication();
        userName = (TextView) findViewById(R.id.userName);
        watch = (Button) findViewById(R.id.watch);

        myId = ((MyApplication) getApplication()).getId();  //我的 id
        //friends = ((MyApplication)getApplication()).getFriends();
        watchList = myApplication.getWatchList();
        for(Friend friend : watchList){//-----------------------------------
            if(friend.getType() != 0 && friend.getId().equals(hisId)){
                watch.setText("取消关注");
                watch.setEnabled(false);
                //watch.setBackgroundColor(Color.parseColor("#D4D4D4"));
            }
        }
        if(hisId.equals(myId)){
            watch.setVisibility(View.GONE);
        }

        userName.setText(hisId);
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchFriend();
            }
        });
    }

    private void watchFriend(){
        String myId = ((MyApplication) getApplication()).getId();
        String hisId = this.hisId;
        HttpUtil.sendHttpRequest("http://123.56.15.171:8080/HelloWorld", myId, hisId, "watch", new HttpCallbackListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("UserActivitytest","ressponse ="+response);
                Gson gson = new Gson();
                final Info info = gson.fromJson(response, Info.class);
                //----------------获得新的关注/粉丝列表---------------
                myApplication.getFansList().clear();
                for (Friend friend : info.getFriends() ){
                    if(friend.getType() != 1){
                        myApplication.getFansList().add(friend);
                    }
                }
                myApplication.getWatchList().clear();
                for (Friend friend : info.getFriends() ){
                    if(friend.getType() != 0){
                        myApplication.getWatchList().add(friend);
                    }
                }
                //-----------------------------
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //-----------------回调更新关注/粉丝列表
                        FriendWatchFragment.updateFriends();
                        FriendFansFragment.updateFriends();
                    }
                });
                if(info.getResult().equals("success")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            watch.setText("取消关注");
                            watch.setEnabled(false);
                            //watch.setBackgroundColor(Color.parseColor("#D4D4D4"));
                        }
                    });
                }
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }


}
