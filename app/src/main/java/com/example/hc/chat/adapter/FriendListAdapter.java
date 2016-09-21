package com.example.hc.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hc.chat.activity.ChatActivity;
import com.example.hc.chat.MyApplication;
import com.example.hc.chat.R;
import com.example.hc.chat.data.Friend;
import com.example.hc.chat.data.MyMessage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hc on 2016/9/2.
 */
public class FriendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Friend> friends;

    public FriendListAdapter(Context context, List<Friend> friends){
        this.context = context;
        this.friends = friends;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        switch (viewType){
            case 0:
                FrdHolder frdHolder = new FrdHolder(LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false));
                return frdHolder;
            default:
                return null;
        }
    }

    @Override
    public  void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
        final String frdId = friends.get(position).getId();
        ((FrdHolder)holder).frdId.setText(frdId);
        if(friends.get(position).getType() == 2){((FrdHolder)holder).frdTag.setVisibility(View.VISIBLE);}
        ((FrdHolder) holder).ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                MyApplication myApplication = (MyApplication) ((Activity)context).getApplication();
                List<MyMessage> messages = myApplication.getMessagesMap().get(frdId);
                intent.putExtra("messages", (Serializable) messages);
                intent.putExtra("frdId", frdId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    private class FrdHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll;
        private TextView frdId;
        private TextView frdTag;
        public FrdHolder(View view){
            super(view);
            frdId = (TextView) view.findViewById(R.id.frdId);
            ll = (LinearLayout) view.findViewById(R.id.ll);
            frdTag = (TextView) view.findViewById(R.id.friend_tag);
        }
    }

}
