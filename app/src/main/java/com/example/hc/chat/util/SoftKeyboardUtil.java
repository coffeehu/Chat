package com.example.hc.chat.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by hc on 2016/10/16.
 */
public class SoftKeyboardUtil {

    private Activity activity;
    private EditText editText;
    private InputMethodManager inputMethodManager;

    public SoftKeyboardUtil(Activity activity, EditText editText){
        this.activity = activity;
        this.editText = editText;
        inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public boolean softKeyboardIsShown(){
        Log.d("emotiontest","softkeyboardHeight :"+getSoftKeyboardHeight());
        return getSoftKeyboardHeight() != 0;
    }

    public void hideSoftKeyboard(){
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        // ((Activity)context).getWindow().getDecorView().getWindowToken() 也可
    }

    public void showSoftKeyboard(){
        inputMethodManager.showSoftInput(editText, 0);
    }

    public int getSoftKeyboardHeight(){
        Rect r = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        int softkeyboardHeight = screenHeight - r.bottom;
        return softkeyboardHeight;
    }



}
