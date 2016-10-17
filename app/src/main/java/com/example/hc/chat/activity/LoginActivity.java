package com.example.hc.chat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hc.chat.util.ConnectionThread;
import com.example.hc.chat.data.LoginMessage;
import com.example.hc.chat.util.HttpCallbackListener;
import com.example.hc.chat.util.HttpUtil;
import com.example.hc.chat.data.Info;
import com.example.hc.chat.MyApplication;
import com.example.hc.chat.R;
import com.google.gson.Gson;

/**
 * Created by hc on 2016/8/31.
 *
 * 登录界面
 * 通过 HttpURLConnection 发送 post 到 服务器（Servlet）
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{
    private MyApplication myApplication;
    private static final String IP = "120.25.235.230";
    private static final int PORT = 55555;
    private ConnectionThread connectionThread;

    private EditText id, psw;
    private Button start;
    private Toolbar toolbar;
    private TextInputLayout textInputLayout_id, textInputLayout_psw;
    private CheckBox checkBox;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String myId, myPsw;
    private Gson gson;
    private Info info;

    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
        myApplication = (MyApplication) getApplication();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); // 返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 返回箭头

        textInputLayout_id = (TextInputLayout) findViewById(R.id.textinputlayout_id);
        textInputLayout_id.setHint("请输入用户名:"); //必须要 setHint() 后才能生效
        textInputLayout_psw = (TextInputLayout) findViewById(R.id.textinputlayout_psw);
        textInputLayout_psw.setHint("请输入密码:");

        id = (EditText) findViewById(R.id.id);
        id.addTextChangedListener(this); //监听输入内容状态
        psw = (EditText) findViewById(R.id.psw);
        psw.addTextChangedListener(this);
        start = (Button) findViewById(R.id.start);
        start.setEnabled(false);
        start.setOnClickListener(this);

        // 注册后会跳转到 login 页面，并且会把注册的 id 传过来，我想让 id 显示到 login 页面的用户名输入框。
        Intent intent = getIntent();
        String tmpId = intent.getStringExtra("id");

        checkBox = (CheckBox) findViewById(R.id.checkbox);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        boolean isRemember = sharedPreferences.getBoolean("isRemember", false);
        if(tmpId == null) { // 这就表示不是由注册页面跳转过来的
            if (isRemember) {  // 如果保存了密码 用户名 信息，则取出
                id.setText(sharedPreferences.getString("id", ""));
                psw.setText(sharedPreferences.getString("password", ""));
                checkBox.setChecked(true);
            }
        }else {
            editor.clear();
            id.setText(tmpId);
        }
        gson = new Gson();
        info = new Info();
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.start:
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("登录中");
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);
                progressDialog.show();

                myId = id.getText().toString();
                myPsw = psw.getText().toString();
                /**
                 * 封装的 HttpURLConnection 类，发送 post 请求到 Servelet。
                 * 参数： 地址， id ，密码，类型（login / register / select / watch 4 种），回调接口
                 */
                HttpUtil.sendHttpRequest("http://120.25.235.230:8080/HelloWorld", myId, myPsw, "login", new HttpCallbackListener() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("tmptestt","response :"+response);
                        Info info = gson.fromJson(response, Info.class);
                        if(info.getResult().equals("success")){ //--------登录成功
                            initConnectionThread();

                            //记住密码功能，存储在 sharedPreferences 中
                            if(checkBox.isChecked()){ // 如果选中“记住密码”，则将信息存入
                                editor.putBoolean("isRemember",true);
                                editor.putString("id",myId);
                                editor.putString("password",myPsw);
                            }else {editor.clear();} // 如果没选，就情空
                            editor.commit(); //记得提交

                            // 设置好友列表信息存入 application 变量
                            myApplication.setFriends(info.getFriends());
                            myApplication.setId(info.getId());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(info.getResult().equals("fail")){   //--------登录失败
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "登录失败,请检查用户名和密码", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    @Override
                    public void onFail(Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
    }

    public void initConnectionThread(){
        /**
         * 连接 socket 后，发送信息给 Server。
         * Server 收到信息后会把一个 connectionTread 加入 hashmap，key 就是这个用户的 id
         */
        LoginMessage loginMessage = new LoginMessage();
        loginMessage.setId(myId);
        loginMessage.setType("login");
        Gson gson = new Gson();
        String json = gson.toJson(loginMessage);
        connectionThread = new ConnectionThread(IP, PORT);
        connectionThread.connect();
        //connectionThread.addMessageListener(listener);
        myApplication.setConnectionThread(connectionThread);
        connectionThread.sendMessage(json);
    }


    //顶部 toolbar 返回箭头的监听事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // EditView 的输入内容状态监听，活动要实现 TextWatcher；对应的 EditView 要 addTextChangedListener(context)
    // 三者一直循环调用，当你输入了 2 个字母，可能已经分别被调用了 2 次
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (id.getText().toString() != null && id.getText().toString().length()>0 && psw.getText().toString() != null && psw.getText().toString().length()>0 ){
            start.setEnabled(true); // 当用户名和密码都不为空时，按钮设为 enabled 状态
        } else {
            start.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog != null) progressDialog.dismiss();

            }
        });

    }

}
