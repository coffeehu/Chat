package com.example.hc.chat.view;

import android.content.Intent;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hc.chat.R;
import com.example.hc.chat.activity.SearchActivity;
import com.example.hc.chat.fragment.FriendFansFragment;
import com.example.hc.chat.fragment.FriendWatchFragment;

/**
 * Created by hc on 2016/9/8.
 *
 * friend 页面的标题栏控件，靠左是一个 tab（关注 和 粉丝），最右 add 按钮
 */
public class MainTitleForFriend extends LinearLayout{

    private ImageView add;
    private static TabLayout tabLayout;
//    private FragmentManager manager;
    private FriendFansFragment fff;
    private FriendWatchFragment fwf;

    public MainTitleForFriend(Context context){
        this(context, null);
    }

    public MainTitleForFriend(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.main_title_friend, this);

        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                getContext().startActivity(intent);
            }
        });


        tabLayout = (TabLayout) findViewById(R.id.tl);

        TabLayout.Tab watch = tabLayout.newTab();
        watch.setText("关注");
        tabLayout.addTab(watch);

        TabLayout.Tab fans = tabLayout.newTab();
        fans.setText("粉丝");
        tabLayout.addTab(fans);

    }


    public static void setTabClick(TabLayout.OnTabSelectedListener listener){
        tabLayout.setOnTabSelectedListener(listener);
    }

}
