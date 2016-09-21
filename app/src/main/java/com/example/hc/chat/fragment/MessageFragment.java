package com.example.hc.chat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hc.chat.util.ConnectionThread;
import com.example.hc.chat.MyApplication;
import com.example.hc.chat.data.MessageItem;
import com.example.hc.chat.data.MyMessage;
import com.example.hc.chat.R;
import com.example.hc.chat.adapter.MessageListAdapter;
import com.example.hc.chat.view.MyItemDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hc on 2016/9/7.
 */
public class MessageFragment extends Fragment {
    private MyApplication myApplication;
    private ConnectionThread connectionThread;
    //private HashMap<String, List<MyMessage>> messagesMap = new HashMap<>();
    private MessageItem messageItem;
    private List<MessageItem> messageItemList = new ArrayList<>();
    private List<String> messageItemName = new ArrayList<>();

    private RecyclerView recyclerView;
    private static MessageListAdapter adapter;

    private ConnectionThread.OnMessageListener listener = new ConnectionThread.OnMessageListener() {
        @Override
        public void onReceive(String data) {
            Log.d("chattesttt"," 1.messagefrag onReceive(),data = "+data);
            Gson gson = new Gson();

            /**
             * 一个 map， { 用户名 ：MyMessage 集合 }
             */
            HashMap<String,List<MyMessage>> messagesMap = myApplication.getMessagesMap();
            MyMessage myMessage = gson.fromJson(data, MyMessage.class);
            if(messagesMap.get(myMessage.getId()) == null) {
                List<MyMessage> messages = new ArrayList<>();
                messages.add(myMessage);
                messagesMap.put(myMessage.getId(), messages);
            }else {
                messagesMap.get(myMessage.getId()).add(myMessage);
            }

            //myApplication.setMessagesMap(messagesMap);


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });



            /*
            if(messageItemName.size()>0){
                for(int i=0;i<messageItemName.size();i++){
                    int size = messagesMap.get(messageItemName.get(i)).size() - 1;
                    Log.d("---chattestt","frdId :"+messageItemName.get(i)+",number :"+messagesMap.get(messageItemName.get(i)).size()+
                            ",lastestContent :"+messagesMap.get(messageItemName.get(i)).get(size).getContent() );
                }
            }
            */


        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        myApplication = (MyApplication) getActivity().getApplication();
        connectionThread = myApplication.getConnectionThread();
        connectionThread.addMessageListener(listener);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_message_recyclerview);
        recyclerView.addItemDecoration(new MyItemDecoration(getContext(), MyItemDecoration.VERTICAL_LIST));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);



        adapter = new MessageListAdapter(getActivity(), myApplication.getMessagesMap());
        recyclerView.setAdapter(adapter);


        return view;
    }

    public static void notifyAdater(){
        adapter.notifyDataSetChanged();
    }
}
