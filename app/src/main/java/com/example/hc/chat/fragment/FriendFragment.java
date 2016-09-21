package com.example.hc.chat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hc.chat.R;
import com.example.hc.chat.activity.MainActivity;
import com.example.hc.chat.view.MainTitleForFriend;

/**
 * Created by hc on 2016/9/7.
 */
public class FriendFragment extends Fragment {
    private FriendFansFragment fff;
    private FriendWatchFragment fwf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        //获取FragmentManager对象
        FragmentManager manager = getChildFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        fwf = new FriendWatchFragment();
        transaction.replace(R.id.friend_content, fwf);
        transaction.commit();

        MainTitleForFriend.setTabClick(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("关注")) {
                    showTab(0);
                }else if(tab.getText().equals("粉丝")){
                    showTab(1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }


    /**
     *
     * @param i
     * 0 = 关注
     * 1 = 粉丝
     */
    public void showTab(int i){
        //FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();//获取FragmentManager对象
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (i){
            case 0: // 关注，显示 fwf
                if(fff != null) transaction.hide(fff);
                if(fwf == null) {
                    fwf = new FriendWatchFragment();
                    transaction.add(R.id.friend_content, fwf);
                }else {
                    transaction.show(fwf);
                }
                break;
            case 1: // 粉丝，显示 fff
                if(fwf != null) transaction.hide(fwf);
                if(fff == null) {
                    fff = new FriendFansFragment();
                    transaction.add(R.id.friend_content, fff);
                }else {
                    transaction.show(fff);
                }
                break;
        }
        transaction.commit();
    }


}
