package com.example.hc.chat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hc.chat.adapter.FriendListAdapter;
import com.example.hc.chat.MyApplication;
import com.example.hc.chat.R;
import com.example.hc.chat.data.Friend;
import com.example.hc.chat.view.MyItemDecoration;

import java.util.List;

/**
 * Created by hc on 2016/9/8.
 *
 * 关注列表。
 * Friend 的 type，0 为单向关注我
 * 1 为我单向关注他
 * 2 为互相关注
 */
public class FriendWatchFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private static FriendListAdapter adapter;
    private List<Friend> watchList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend_watch, container, false);
        init();
        return view;
    }

    private void init(){
        recyclerView = (RecyclerView) view.findViewById(R.id.friend_watch);
        recyclerView.addItemDecoration(new MyItemDecoration(getContext(), MyItemDecoration.VERTICAL_LIST));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        watchList = ((MyApplication)getActivity().getApplication()).getWatchList();
        adapter = new FriendListAdapter(getContext(), watchList);
        recyclerView.setAdapter(adapter);
    }

    public static void updateFriends(){
        adapter.notifyDataSetChanged();
    }


}