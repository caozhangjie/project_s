package com.example.aslan.mybluetootharduino;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

/**
 * Created by caozj on 8/19/15.
 */
public class netResult extends Application {
    private String get_result;
    private String post_result;
    public BluetoothSocket my_bluetooth_socket;
    private boolean get_finish;
    private boolean post_finish;
    private int userid;
    private FootLanguage footLanguage;
    private LocationClient locationClient;
    private MyLocationListener listener_for_now;
    private double now_latitude;
    private double now_longitude;
    private Handler handler;
    public DictionaryOpenHelper helper;

    @Override
    public void onCreate(){
        super.onCreate();
        my_bluetooth_socket = null;
        footLanguage = new FootLanguage();
        helper = new DictionaryOpenHelper(getApplicationContext());
        get_finish = true;
        post_finish = true;
        locationClient = new LocationClient(this.getApplicationContext());
        listener_for_now = new MyLocationListener();
        locationClient.registerLocationListener(listener_for_now);

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            now_latitude = location.getLatitude();
            now_longitude = location.getLongitude();
            Bundle data = new Bundle();
            data.putDouble("latitude", now_latitude);
            data.putDouble("longitude", now_longitude);
            Message m = handler.obtainMessage();
            m.setData(data);
            handler.sendMessage(m);
        }
    }
    LocationClient getLocationClient(){return locationClient;}
    FootLanguage getFootLanguage(){return footLanguage;}



    void setHandler(Handler h){
        handler = h;
    }

    double getNow_latitude(){return now_latitude;}

    double getNow_longitude(){return now_longitude;}

    DictionaryOpenHelper getHelper(){return helper;}

    public void setUserid(int i){ userid = i;}

    public int getUserid(){return userid;}

    public boolean isGet_finish(){
        return get_finish;
    }

    public void setGet_finish(boolean f){
        get_finish = f;
    }

    public void setPost_finish(boolean F){
        post_finish = F;
    }

    public boolean isPost_finish(){
        return post_finish;
    }

    public String getGet_result(){
        return get_result;
    }

    public String getPost_result(){
        return post_result;
    }

    public void setGet_result(String result){
        get_result = new String(result);
        setGet_finish(true);
    }

    public void setPost_result(String result){
        post_result = new String(result);
        setPost_finish(true);
    }
}
