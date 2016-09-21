package com.example.hc.chat.activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hc.chat.R;

import java.util.List;

/**
 * Created by hc on 2016/9/14.
 */
public class FindNearActivity extends AppCompatActivity {
    private TextView positionTextView;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_findnear);

        positionTextView = (TextView) findViewById(R.id.findnear_text);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
/*
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
            Toast.makeText(this, "No location provider to use", Toast.LENGTH_SHORT).show();
            return;
        }*/
        String provider = LocationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            // 显示当前设备的位置信息
            showLocation(location);
        }else{
            Log.d("findnearactivitytest","location = null");
        }
       // locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);

    }



    private void showLocation(Location location) {
        String currentPosition = "latitude is " + location.getLatitude() + "\n" + "longitude is " + location.getLongitude();
        positionTextView.setText(currentPosition);
    }

}
