package com.example.hc.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.hc.chat.R;

/**
 * Created by hc on 2016/9/4.
 */
public class SplashActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private Button register, login;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        toolbar.setTitle("启动");
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.register:
                intent = new Intent(SplashActivity.this, RegActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //android.os.Process.killProcess(android.os.Process.myPid());  立即结束该 app 进程
    }



}
