package com.example.hc.chat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hc.chat.R;
import com.example.hc.chat.activity.ChatActivity;
import com.example.hc.chat.data.MyMessage;

import java.util.List;

/**
 * Created by hc on 2016/9/3.
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int ME = 0;
    private static final int FRD = 1;
    private Context context;
    private List<MyMessage> messages;

    public ChatListAdapter(Context context, List<MyMessage> messages){
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getLocalType().equals("me")){
            return ME;
        }else{
            return FRD;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        switch (viewType){
            case ME:
                return new MeHolder(LayoutInflater.from(context).inflate(R.layout.chatlist_item_me, parent, false));
            case FRD:
                return new FrdHolder(LayoutInflater.from(context).inflate(R.layout.chatlist_item_frd, parent, false));
            default:
                return null;
        }
    }

    @Override
    public  void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
        if(messages.size()>0) {
            final String content = messages.get(position).getContent();
            String bitmapString = messages.get(position).getBitmapString();
            Log.d("tmptesttest","bitmapString : "+bitmapString);
            if (holder instanceof MeHolder) {
                if(bitmapString != null){
                    Bitmap bitmap = ChatActivity.base64ToBitmap(bitmapString);
                    ChatActivity.zoomImage(bitmap,80,80);
                    ((MeHolder)holder).meimg.setVisibility(View.VISIBLE);
                    ((MeHolder)holder).metext.setVisibility(View.GONE);
                    ((MeHolder)holder).meimg.setImageBitmap(bitmap);
                }else {
                    ((MeHolder)holder).meimg.setVisibility(View.GONE);
                    ((MeHolder)holder).metext.setVisibility(View.VISIBLE);
                    ((MeHolder) holder).metext.setText(content);
                }
            } else if (holder instanceof FrdHolder) {
                if(bitmapString  != null){
                    Log.d("chatimgtest","bitmap : "+bitmapString);
                    Bitmap bitmap = ChatActivity.base64ToBitmap(bitmapString);
                    ChatActivity.zoomImage(bitmap,80,80);
                    ((FrdHolder)holder).frdimg.setVisibility(View.VISIBLE);
                    ((FrdHolder)holder).frdtext.setVisibility(View.GONE);
                    ((FrdHolder)holder).frdimg.setImageBitmap(bitmap);
                }else {
                    ((FrdHolder)holder).frdimg.setVisibility(View.GONE);
                    ((FrdHolder)holder).frdtext.setVisibility(View.VISIBLE);
                    ((FrdHolder) holder).frdtext.setText(content);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if(messages!=null) {
            return messages.size();
        }else {
            return 0;
        }
    }

    private class FrdHolder extends RecyclerView.ViewHolder {
        private TextView frdtext;
        private ImageView frdimg;
        public FrdHolder(View view){
            super(view);
            frdtext = (TextView) view.findViewById(R.id.frdtext);
            frdimg = (ImageView) view.findViewById(R.id.frdimg);
        }
    }

    private class MeHolder extends RecyclerView.ViewHolder {
        private TextView metext;
        private ImageView meimg;
        public MeHolder(View view){
            super(view);
            metext = (TextView) view.findViewById(R.id.metext);
            meimg = (ImageView) view.findViewById(R.id.meimg);
        }
    }


}
