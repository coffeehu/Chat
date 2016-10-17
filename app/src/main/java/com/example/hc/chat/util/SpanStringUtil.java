package com.example.hc.chat.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hc on 2016/10/16.
 */
public class SpanStringUtil {


    public static SpannableString getEmotion(Context context, String content, TextView textView){
        SpannableString spannableString = new SpannableString(content);
        String zz = "\\[([/\u4e00-\u9fa5\\w])+\\]";
        Matcher matcher = Pattern.compile(zz).matcher(spannableString);

        while (matcher.find()){
            String rs = matcher.group();
            int start = matcher.start();
            Log.d("spantest","matcher.group() :"+rs+",matcher.start() :"+start);
            int img = EmotionUtil.getImgByName(rs);
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), img);
            int size = (int) textView.getTextSize()*13/10;
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
            ImageSpan imageSpan = new ImageSpan(context, scaleBitmap);
            spannableString.setSpan(imageSpan, start, start + rs.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
