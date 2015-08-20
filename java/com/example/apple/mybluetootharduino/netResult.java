package com.example.apple.mybluetootharduino;

import android.app.Application;

/**
 * Created by caozj on 8/19/15.
 */
public class netResult extends Application {
    private String get_result;
    private String post_result;
    private boolean get_finish;
    private boolean post_finish;

    @Override
    public void onCreate(){
        super.onCreate();
        get_finish = true;
        post_finish = true;
    }

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
