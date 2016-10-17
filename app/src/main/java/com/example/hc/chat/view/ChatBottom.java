package com.example.hc.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hc.chat.R;

/**
 * Created by hc on 2016/9/19.
 */
public class ChatBottom extends LinearLayout {
    public Button button;
    public ImageView sendimg, emotionButton;
    public EditText editText;

    public ChatBottom(Context context){
        super(context);
    }

    public ChatBottom(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.chat_bottom, this);
        button = (Button) findViewById(R.id.button);
        sendimg = (ImageView) findViewById(R.id.sendimg);
        editText = (EditText) findViewById(R.id.edit);
        emotionButton = (ImageView) findViewById(R.id.emotion_button);
    }

}
