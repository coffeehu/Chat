package com.example.hc.chat.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Created by hc on 2016/10/16.
 */
public class EmotionPagerAdapter extends PagerAdapter {
    private List<GridView> emotionViews;

    public EmotionPagerAdapter(List<GridView> emotionViews){
        this.emotionViews = emotionViews;
    }


    @Override
    public int getCount() {
        return emotionViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(emotionViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(emotionViews.get(position));
        return emotionViews.get(position);
    }
}
