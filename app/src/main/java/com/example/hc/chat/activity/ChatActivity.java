package com.example.hc.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hc.chat.adapter.ChatListAdapter;
import com.example.hc.chat.adapter.EmotionPagerAdapter;
import com.example.hc.chat.adapter.GridViewAdapter;
import com.example.hc.chat.util.ConnectionThread;
import com.example.hc.chat.MyApplication;
import com.example.hc.chat.R;
import com.example.hc.chat.data.MyMessage;
import com.example.hc.chat.fragment.MessageFragment;
import com.example.hc.chat.util.EmotionUtil;
import com.example.hc.chat.util.SoftKeyboardUtil;
import com.example.hc.chat.util.SpanStringUtil;
import com.example.hc.chat.view.ChatBottom;
import com.example.hc.chat.view.MainTitle;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  聊天界面
 */

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{
    private MyApplication myApplication;

    private String frdId; //对方 id
    private String id; //我的 id
    private ConnectionThread connectionThread;
    private List<MyMessage> messages = new ArrayList<>();
    private List<GridView> emotionViews;
    private List<String> emotionNames;
    private int softKeyboardHeight;

    private RecyclerView chatList;
    private ChatListAdapter adapter;
    private EditText editText;
    private Button button;
    private ImageView sendimg, emotion_button;
    private MainTitle title;
    private LinearLayout chat_ll, chat_emotion_ll;
    private ChatBottom chat_bottom;
    private ViewPager chat_emotion_vp;
    private int chat_emotion_ll_height;

    private SoftKeyboardUtil softKeyboardUtil;

    private ConnectionThread.OnMessageListener listener = new ConnectionThread.OnMessageListener() {
        @Override
        public void onReceive(final String data) {
            //
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //------------ Log.d("chattest", "main onreceive() ,main listener "+myMessage.id+""+myMessage.content);
                    Gson gson = new Gson();
                    MyMessage myMessage = gson.fromJson(data, MyMessage.class);
                    if(myMessage.getId().equals(frdId)) {
                        messages.add(myMessage);
                        adapter.notifyDataSetChanged();
                        chatList.smoothScrollToPosition(messages.size()-1);
                       // myApplication.getMessagesMap().get(frdId).add(myMessage);
                    }
                    //textView.append(myMessage.getId()+" : "+myMessage.getContent()+"\n");
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        myApplication = (MyApplication) getApplication();

        softKeyboardHeight = myApplication.getSoftKeyboardHeight();

        Intent intent = getIntent();
        messages = (ArrayList<MyMessage>) intent.getSerializableExtra("messages");
        if(messages == null) messages = new ArrayList<>();

        frdId = getIntent().getStringExtra("frdId");
        id = ((MyApplication)getApplication()).getId();
        connectionThread = ((MyApplication)getApplication()).getConnectionThread();//------------------------
        connectionThread.addMessageListener(listener);

        initView();
        initEmotionData();
        initListener();
        adapter = new ChatListAdapter(ChatActivity.this, messages);
        chatList.setAdapter(adapter);

        EmotionPagerAdapter emotionPagerAdapter = new EmotionPagerAdapter(emotionViews);
        chat_emotion_vp.setAdapter(emotionPagerAdapter);
    }

    private void initView(){
        chat_bottom = (ChatBottom) findViewById(R.id.chat_bottom);
        editText = chat_bottom.editText;
        editText.addTextChangedListener(this);

        softKeyboardUtil = new SoftKeyboardUtil(ChatActivity.this, editText);

        button = chat_bottom.button;
        button.setOnClickListener(this);
        sendimg = chat_bottom.sendimg;
        sendimg.setOnClickListener(this);
        chatList = (RecyclerView) findViewById(R.id.chatList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        chatList.setLayoutManager(linearLayoutManager);
        title = (MainTitle) findViewById(R.id.chat_title);
        title.setTitle("与"+frdId+"对话");

        chat_emotion_ll =  (LinearLayout) findViewById(R.id.chat_emotion_ll);

        emotion_button = chat_bottom.emotionButton;
        emotion_button.setOnClickListener(this);

        chat_ll = (LinearLayout) findViewById(R.id.chat_ll);

        chat_emotion_vp = (ViewPager) findViewById(R.id.chat_emotion_vp);
    }


    private void initEmotionData(){
        emotionViews = new ArrayList<>();
        emotionNames = new ArrayList<>();
        for(String emojiName : EmotionUtil.getEmojiMap().keySet()){
            emotionNames.add(emojiName);
            if(emotionNames.size() == 20){
                GridView gv = createGridView(emotionNames);
                emotionViews.add(gv);
                emotionNames = new ArrayList<>();
            }
        }

        if(emotionNames.size() > 0){
            GridView gv = createGridView(emotionNames);
            emotionViews.add(gv);
        }
    }

    private GridView createGridView( List<String> emotionNames){
        GridView gv = new GridView(ChatActivity.this);
        gv.setNumColumns(7); // 7 列

        //--------;
        gv.setPadding(10, 50, 10, 50);
        //gv.setHorizontalSpacing(50);
        gv.setVerticalSpacing(50); //两行之间的间距
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100, 100);
        gv.setLayoutParams(params); // ?????? 去掉也无影响
        //--------

        GridViewAdapter adapter = new GridViewAdapter(ChatActivity.this, emotionNames);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridViewAdapter gridViewAdapter = (GridViewAdapter) parent.getAdapter();
                String emotionName = gridViewAdapter.getItem(position);

                int curPosition = editText.getSelectionStart();
                StringBuilder sb = new StringBuilder(editText.getText().toString());
                sb.insert(curPosition, emotionName);
                editText.setText(SpanStringUtil.getEmotion(ChatActivity.this, sb.toString(), editText));
                editText.setSelection(curPosition + emotionName.length());
                //Toast.makeText(getActivity(),emotionName,Toast.LENGTH_SHORT).show();
            }
        });
        return gv;
    }


    private void initListener(){

        chat_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                ChatActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) chatList.getLayoutParams();

                if(softKeyboardUtil.getSoftKeyboardHeight() !=0) softKeyboardHeight = softKeyboardUtil.getSoftKeyboardHeight();
                Log.d("emotiontest","softh :"+softKeyboardHeight);
                LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) chat_emotion_ll.getLayoutParams();
                if(softKeyboardHeight != lp2.height) {
                    lp2.height = softKeyboardHeight;
                    chat_emotion_ll.setLayoutParams(lp2);
                }



                int statusBarHeight = r.top;

                if(chat_emotion_ll.isShown()){
                    chat_emotion_ll_height = chat_emotion_ll.getHeight();
                }else {
                    chat_emotion_ll_height = 0;
                }

                int h = r.bottom - title.getHeight() - chat_bottom.getHeight() - statusBarHeight - chat_emotion_ll_height;


                //Log.d("emotiontest","emotionllheight :"+chat_emotion_ll_height);
                //Log.d("emotiontest","softkeyboardheight :"+softKeyboardUtil.getSoftKeyboardHeight());


                if(h != lp.height){
                    lp.height = h;
                    //chat_ll.requestLayout();
                    chatList.setLayoutParams(lp);
                }

            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(chat_emotion_ll.isShown()) chat_emotion_ll.setVisibility(View.GONE);
                return false;
            }
        });

    }


    //handler.obtainMessage(0, data).sendToTarget();

    private void send(){
                try {
                    String mContent = editText.getText().toString();
                    if(mContent.trim() != null) {
                        String mDestId = frdId;
                        editText.setText("");
                        Log.d("chattest", "main  send(), content =" + mContent + ",destid =" + mDestId);
                        MyMessage myMessage = new MyMessage();
                        myMessage.setId(id);
                        myMessage.setDestId(frdId);
                        myMessage.setContent(mContent);
                        messages.add(myMessage);

                        HashMap<String, List<MyMessage>> messagesMap = myApplication.getMessagesMap();
                        if (messagesMap.get(myMessage.getDestId()) == null) {
                            List<MyMessage> messages = new ArrayList<>();
                            messages.add(myMessage);
                            messagesMap.put(myMessage.getDestId(), messages);
                        } else {
                            messagesMap.get(myMessage.getDestId()).add(myMessage);
                        }

                        adapter.notifyDataSetChanged();
                        MessageFragment.notifyAdater(); // 新增了条信息，那么 MessageFragment 也要更新
                        chatList.smoothScrollToPosition(messages.size()-1);
                        connectionThread.sendMessage(myMessage);
                    }
                }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button:
                send();
                sendimg.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                break;
            case R.id.sendimg:
                sendImg();
                break;
            case R.id.emotion_button:
                initEmotionBar();
                break;
            default:
                break;
        }
    }


    private void initEmotionBar(){
        if(chat_emotion_ll.isShown()){
            chat_emotion_ll.setVisibility(View.GONE);
            softKeyboardUtil.showSoftKeyboard();
        }else {
//            if(softKeyboardUtil.softKeyboardIsShown()) softKeyboardUtil.hideSoftKeyboard();
            softKeyboardUtil.hideSoftKeyboard();
            chat_emotion_ll.setVisibility(View.VISIBLE);
        }
    }





    private void sendImg(){

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        String imgData = bitmapToBase64(bitmap);
        MyMessage myMessage = new MyMessage();
        myMessage.setId(id);
        myMessage.setDestId(frdId);
        myMessage.setBitmapString(imgData);
        messages.add(myMessage);
        adapter.notifyDataSetChanged();
        chatList.smoothScrollToPosition(messages.size()-1);
        connectionThread.sendMessage(myMessage);

        HashMap<String, List<MyMessage>> messagesMap = myApplication.getMessagesMap();
        if (messagesMap.get(myMessage.getDestId()) == null) {
            List<MyMessage> messages = new ArrayList<>();
            messages.add(myMessage);
            messagesMap.put(myMessage.getDestId(), messages);
        } else {
            messagesMap.get(myMessage.getDestId()).add(myMessage);
        }


       // connectionThread.sendMessage(imgData);


    }


    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
     // 获取这个图片的宽和高
           float width = bgimage.getWidth();
         float height = bgimage.getHeight();
                 // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
              // 计算宽高缩放率
              float scaleWidth = ((float) newWidth) / width;
               float scaleHeight = ((float) newHeight) / height;
             // 缩放图片动作
              matrix.postScale(scaleWidth, scaleHeight);
            Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                                    (int) height, matrix, true);
                return bitmap;
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
        if(editText.getText().toString() != null && editText.getText().toString().length() > 0){
            sendimg.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }else {
            sendimg.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectionThread.removeMessageListener(listener);
    }



}
