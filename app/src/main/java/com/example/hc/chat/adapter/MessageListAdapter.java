package com.example.hc.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hc.chat.activity.ChatActivity;
import com.example.hc.chat.MyApplication;
import com.example.hc.chat.data.MyMessage;
import com.example.hc.chat.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hc on 2016/9/11.
 */
public class MessageListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private MyApplication myApplication;
    private HashMap<String, List<MyMessage>> messagesMap;

    public MessageListAdapter(Context context, HashMap<String, List<MyMessage>> messagesMap){
        this.context = context;
        this.messagesMap = messagesMap;
        myApplication = (MyApplication)((Activity)context).getApplication();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        switch (viewType){
            case 0:
                MsgHolder msgHolder = new MsgHolder(LayoutInflater.from(context).inflate(R.layout.messagelist_item, parent, false));
                return msgHolder;
            default:
                return null;
        }
    }

    @Override
    public  void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position){
        List<String> messageItemName = new ArrayList<>();
        for (Map.Entry<String, List<MyMessage>> entry : messagesMap.entrySet()) {
            messageItemName.add(entry.getKey());
        }
        final String frdId = messageItemName.get(position);
        final List<MyMessage> messages = messagesMap.get(frdId);
        final int number = messages.size();
        String latestContent = messages.get(number - 1).getContent();
        ((MsgHolder)holder).frdIdText.setText(frdId);
        ((MsgHolder)holder).numberText.setText(number+"");
        ((MsgHolder)holder).latestContentText.setText(latestContent);
        ((MsgHolder)holder).rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("frdId", frdId);
                intent.putExtra("messages", (Serializable) messages);
                ((MsgHolder)holder).numberText.setText("");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return messagesMap.size();
    }

    private class MsgHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl;
        private TextView frdIdText;
        private TextView numberText;
        private TextView latestContentText;
        public MsgHolder(View view){
            super(view);
            frdIdText = (TextView) view.findViewById(R.id.frdId);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            numberText = (TextView) view.findViewById(R.id.number);
            latestContentText = (TextView) view.findViewById(R.id.latestContent);
        }
    }

}