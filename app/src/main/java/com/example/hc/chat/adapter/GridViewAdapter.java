package com.example.hc.chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.hc.chat.R;
import com.example.hc.chat.util.EmotionUtil;

import java.util.List;

/**
 * Created by hc on 2016/10/16.
 */
public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> emotionNames;

    public GridViewAdapter(Context context, List<String> emotionNames){
        this.context = context;
        this.emotionNames = emotionNames;
    }

    @Override
    public int getCount(){
        return emotionNames.size() + 1;
    }

    @Override
    public String getItem(int position){
        return emotionNames.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Log.d("adaptertest","position :"+position);
        Log.d("adaptertest","getcount() :"+getCount());
        ImageView img = new ImageView(context);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(80, 80);
        img.setLayoutParams(lp);



        if(position == getCount() - 1) {
            img.setImageResource(R.drawable.delete_emotion);
        } else {
            String emotionName = emotionNames.get(position);
            img.setImageResource(EmotionUtil.getImgByName(emotionName));
        }

        return img;
    }

}
