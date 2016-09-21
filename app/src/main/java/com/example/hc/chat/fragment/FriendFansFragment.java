package com.example.hc.chat.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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
 */
public class FriendFansFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private static FriendListAdapter adapter;
    private List<Friend> fansList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend_watch, container, false);
        init();
        return view;
    }

    private void init(){
        recyclerView = (RecyclerView) view.findViewById(R.id.friend_watch);
        recyclerView.addItemDecoration(new MyItemDecoration(getContext(), MyItemDecoration.VERTICAL_LIST));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        fansList = ((MyApplication)getActivity().getApplication()).getFansList();
        adapter = new FriendListAdapter(getContext(), fansList);
        recyclerView.setAdapter(adapter);
    }

    public static void updateFriends(){
        adapter.notifyDataSetChanged();
    }
}