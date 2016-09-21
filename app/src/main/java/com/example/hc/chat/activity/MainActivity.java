package com.example.hc.chat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.example.hc.chat.util.ConnectionThread;
import com.example.hc.chat.data.LoginMessage;
import com.example.hc.chat.MyApplication;
import com.example.hc.chat.R;
import com.example.hc.chat.adapter.MyPagerAdapter;
import com.example.hc.chat.data.Friend;
import com.example.hc.chat.fragment.FindFragment;
import com.example.hc.chat.fragment.FriendFragment;
import com.example.hc.chat.fragment.MeFragment;
import com.example.hc.chat.fragment.MessageFragment;
import com.example.hc.chat.view.IconTextButton;
import com.example.hc.chat.view.MainTitle;
import com.example.hc.chat.view.MainTitleForFriend;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hc on 2016/9/7.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private MyApplication myApplication;
    private ConnectionThread connectionThread;

    private String myId;
    private List<Friend> friends;
    private List<Friend> fansList = new ArrayList<>();
    private List<Friend> watchList = new ArrayList<>();

    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments;
    private IconTextButton message, friend, find, me;
    private MainTitle title;
    private MainTitleForFriend title2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
       // initSocket();
        myApplication = (MyApplication)getApplication();
        connectionThread = myApplication.getConnectionThread();
        myId = myApplication.getId();
        initView();
        initData();
    }



    private void initView(){
        message = (IconTextButton) findViewById(R.id.message);
        message.setOnClickListener(this);
        friend = (IconTextButton) findViewById(R.id.friend);
        friend.setOnClickListener(this);
        find = (IconTextButton) findViewById(R.id.find);
        find.setOnClickListener(this);
        me = (IconTextButton) findViewById(R.id.me);
        me.setOnClickListener(this);
        initImg();
        initFriends();
        message.setIcon(R.drawable.message_pressed);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        title = (MainTitle) findViewById(R.id.title);
        title2 = (MainTitleForFriend) findViewById(R.id.title2);
    }

    private void initImg(){
        message.setIcon(R.drawable.message_normal);
        friend.setIcon(R.drawable.friend_normal);
        find.setIcon(R.drawable.find_normal);
        me.setIcon(R.drawable.me_normal);
    }

    private void initData(){
        fragments = new ArrayList<>();
        fragments.add(new MessageFragment());
        fragments.add(new FriendFragment());
        fragments.add(new FindFragment());
        fragments.add(new MeFragment());
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 1) {
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        initImg();
                        resetTitle();
                        message.setIcon(R.drawable.message_pressed);
                        break;
                    case 1:
                        initImg();
                        setTitle();
                        friend.setIcon(R.drawable.friend_pressed);
                        break;
                    case 2:
                        initImg();
                        resetTitle();
                        find.setIcon(R.drawable.find_pressed);
                        break;
                    case 3:
                        initImg();
                        resetTitle();
                        me.setIcon(R.drawable.me_pressed);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.message:
                initImg();
                resetTitle();
                message.setIcon(R.drawable.message_pressed);
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.friend:
                initImg();
                setTitle();
                friend.setIcon(R.drawable.friend_pressed);
                viewPager.setCurrentItem(1,false);
                break;
            case R.id.find:
                initImg();
                resetTitle();
                find.setIcon(R.drawable.find_pressed);
                viewPager.setCurrentItem(2,false);
                break;
            case R.id.me:
                initImg();
                setTitle();
                me.setIcon(R.drawable.me_pressed);
                viewPager.setCurrentItem(3,false);
                break;
            default:
                break;
        }
    }

    private void resetTitle(){
        if(title.getVisibility() == View.GONE) {
            title.setVisibility(View.VISIBLE);
            title2.setVisibility(View.GONE);
        }
    }

    private void setTitle(){
        title.setVisibility(View.GONE);
        title2.setVisibility(View.VISIBLE);
    }

    private void initFriends(){
        friends = myApplication.getFriends();
        for (Friend friend : friends ){
            if(friend.getType() != 1){
                fansList.add(friend);
            }
        }
        myApplication.setFansList(fansList);
        for (Friend friend : friends ){
            if(friend.getType() != 0){
                watchList.add(friend);
            }
        }
        myApplication.setWatchList(watchList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 退出时发送一个 close 信号，Server 端也执行相应的 disconnect
         */
        LoginMessage loginMessage = new LoginMessage();
        loginMessage.setId(myId);
        loginMessage.setType("close");
        Gson gson = new Gson();
        String json = gson.toJson(loginMessage);
        myApplication.getMessagesMap().clear();
        connectionThread.sendMessage(json);
        try {
            myApplication.getConnectionThread().disconnect();
        }catch (IOException e){e.printStackTrace();}
    }

}
