package com.example.hc.chat.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hc.chat.R;
import com.example.hc.chat.activity.SearchActivity;

/**
 * Created by hc on 2016/9/8.
 *
 * 标题栏控件，最左 back 按钮，最右 add 按钮
 */
public class MainTitle extends LinearLayout {
    private ImageView back, add;
    private TextView titleText;

    public MainTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.main_title, this);
        titleText = (TextView) findViewById(R.id.titletext);
        back = (ImageView) findViewById(R.id.back);
        add = (ImageView) findViewById(R.id.add);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    public void setTitle(String data){
        titleText.setText(data);
    }

}
