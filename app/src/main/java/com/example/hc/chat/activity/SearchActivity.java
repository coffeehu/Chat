package com.example.hc.chat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hc.chat.R;
import com.example.hc.chat.util.HttpCallbackListener;
import com.example.hc.chat.util.HttpUtil;

/**
 * Created by hc on 2016/9/8.
 */
public class SearchActivity extends AppCompatActivity implements TextWatcher {
    private EditText editText;
    private TextView textView;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        editText = (EditText) findViewById(R.id.search_edit);
        textView = (TextView) findViewById(R.id.search_text);
        editText.addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        data = editText.getText().toString();
        textView.setText("搜索用户 : "+data);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendHttpRequest("http://123.56.15.171:8080/HelloWorld", data, null, "select", new HttpCallbackListener() {
                            @Override
                            public void onSuccess(final String response) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(response.equals("notexist")){
                                            showDialog("用户不存在");
                                        }else if(response.equals("exist")){
                                            Intent intent = new Intent(SearchActivity.this, UserActivity.class);
                                            intent.putExtra("userName",data);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFail(Exception e) {
                            }
                        });

            }
        });
    }


    private void showDialog(String text){
        AlertDialog.Builder dialog = new AlertDialog.Builder(SearchActivity.this);
        dialog.setTitle(text);
        dialog.setPositiveButton("OK", new DialogInterface. OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

}
