package com.example.hc.chat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hc.chat.MyApplication;
import com.example.hc.chat.R;

/**
 * Created by hc on 2016/9/7.
 */
public class MeFragment extends Fragment {
    private TextView textView;
    private MyApplication myApplication;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        textView = (TextView)view.findViewById(R.id.fragment_me_id);
        myApplication = (MyApplication)getActivity().getApplication();
        textView.setText(myApplication.getId());
        return view;
    }
}
