package com.example.hc.chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hc.chat.R;
import com.example.hc.chat.activity.FindNearActivity;

/**
 * Created by hc on 2016/9/7.
 */
public class FindFragment extends Fragment {
    private Button findButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        findButton = (Button) view.findViewById(R.id.fragment_find_find);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FindNearActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
