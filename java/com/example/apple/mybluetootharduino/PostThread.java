package com.example.apple.mybluetootharduino;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caozj on 8/14/15.
 */


public class PostThread extends Thread {
    private String urlstr;
    private Context this_context;
    private List<BasicNameValuePair> pair_list;
    private String result;
    private netResult app;
    private Handler handler;
    private BluetoothActivity.function function_type;


    public PostThread(Context con, String url, List<BasicNameValuePair> pairlist, netResult a, Handler h, BluetoothActivity.function f_type){
        super();
        this_context = con;
        urlstr = url;
        pair_list = new ArrayList<BasicNameValuePair>(pairlist);
        app = a;
        handler = h;
        function_type = f_type;
    }

    public PostThread(Context con){
        super();
        this_context = con;
    }

    @Override
    public void run(){
        app.setPost_finish(false);
        try {
            //创建请求并设置属性
            HttpEntity request_entity = new UrlEncodedFormEntity(pair_list);
            HttpPost my_post = new HttpPost(urlstr);
            my_post.setEntity(request_entity);
            //创建客户端并执行请求
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(my_post);
            HttpEntity response_entity = response.getEntity();
            InputStream in = response_entity.getContent();
            byte[] buffer = new byte[1024];
            int bytes = in.read(buffer);
            result = new String(buffer);
            app.setPost_result(result);
            Message m = handler.obtainMessage();
            Bundle data = new Bundle();
            data.putBoolean("state", true);
            m.setData(data);
            handler.sendMessage(m);
        }catch (Exception e){
            app.setPost_finish(true);
            Message m = handler.obtainMessage();
            Bundle data = new Bundle();
            data.putBoolean("state", true);
            data.putString("function_type", function_type.toString());
            m.setData(data);
            handler.sendMessage(m);
            e.printStackTrace();
        }
    }

}
