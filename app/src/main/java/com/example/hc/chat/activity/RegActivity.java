package com.example.hc.chat.activity;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.hc.chat.util.HttpCallbackListener;
import com.example.hc.chat.util.HttpUtil;
import com.example.hc.chat.data.Info;
import com.example.hc.chat.R;
import com.google.gson.Gson;

/**
 * Created by hc on 2016/9/2.
 */
public class RegActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {
    private EditText id, psw;
    private Button register;
    private Toolbar toolbar;
    private TextInputLayout textInputLayout_id, textInputLayout_psw;

    private String myId, myPsw;
    private Gson gson;
    private Info info;

    private boolean registerEnable = false; // 注册按钮是否能按（判断条件之一）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reg);
        initView();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); // 返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 返回箭头

        textInputLayout_id = (TextInputLayout) findViewById(R.id.textinputlayout_id);
        textInputLayout_id.setHint("用户名(只能含有数字和字母):"); //必须要 setHint() 后才能生效
        textInputLayout_psw = (TextInputLayout) findViewById(R.id.textinputlayout_psw);
        textInputLayout_psw.setHint("密码:");

        id = (EditText) findViewById(R.id.id);
        id.addTextChangedListener(this);
        psw = (EditText) findViewById(R.id.psw);
        psw.addTextChangedListener(this);
        psw.setOnFocusChangeListener(this);

        register = (Button) findViewById(R.id.register);
        register.setEnabled(false);
        register.setOnClickListener(this);

        gson = new Gson();
        info = new Info();
    }


    @Override
    public void onClick(View view) {
        myId = id.getText().toString();
        myPsw = psw.getText().toString();
        HttpUtil.sendHttpRequest("http://120.25.235.230:8080/HelloWorld", myId, myPsw, "register", new HttpCallbackListener() {
            @Override
            public void onSuccess(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.equals("registersuccess")){
                            Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new  Intent(RegActivity.this, LoginActivity.class);
                            intent.putExtra("id", myId);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(RegActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    // EditView 输入内容的监听
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (id.getText().toString() != null && id.getText().toString().length()>0 && psw.getText().toString() != null && psw.getText().toString().length()>0 && registerEnable){
            register.setEnabled(true); // 当用户名和密码都不为空时，按钮设为 enabled 状态
        } else {
            register.setEnabled(false);
        }
    }


    // 密码输入框设置了焦点监听
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) { // 得到焦点
            /**
             * 此处为得到焦点时的处理内容
             * 密码框获得焦点时，把用户名 post 到服务端判断
             */
            myId = id.getText().toString();
            //如果用户输入框不为空
            if(myId != null && myId.length()>0) {
                registerEnable = true;
                HttpUtil.sendHttpRequest("http://120.25.235.230:8080/HelloWorld", myId, null, "select", new HttpCallbackListener() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("chattest", "----Reg onFocusChange() response : " + response);
                        // 如果用户名存在
                        if(response.equals("exist")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    registerEnable = false;
                                    textInputLayout_id.setErrorEnabled(true);
                                    textInputLayout_id.setError("用户已存在!");
                                }
                            });
                        // 如果用户名不存在
                        }else if(response.equals("notexist")){
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                    }
                });
            }else {  //如果用户输入框为空
                registerEnable = false;
                textInputLayout_id.setErrorEnabled(true);
                textInputLayout_id.setError("用户名不能为空");
            }


        } else {
            // 此处为失去焦点时的处理内容
            textInputLayout_id.setErrorEnabled(false);
            psw.setText("");
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
