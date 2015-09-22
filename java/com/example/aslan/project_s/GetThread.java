package com.example.aslan.project_s;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

/**
 * Created by caozj on 8/19/15.
 */
public class GetThread extends Thread {
    private String urlstr;
    private Context this_context;
    private String result;
    private netResult app;
    private Handler handler;
    private BluetoothActivity.function function_type;

    public GetThread(Context con, String url, netResult a, Handler h, BluetoothActivity.function f_type){
        super();
        this_context = con;
        urlstr = url;
        app = a;
        handler = h;
        function_type = f_type;
    }

    public GetThread(Context con){
        super();
        this_context = con;
    }


    @Override
    public void run(){

        try {
            //创建请求并设置属性
            HttpGet my_get = new HttpGet(urlstr);
            //创建客户端并执行请求
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(my_get);
            HttpEntity response_entity = response.getEntity();
            InputStream in = response_entity.getContent();
            byte[] buffer = new byte[4096];
            int bytes = in.read(buffer);
            byte[] buffer_data = new byte[bytes];
            for (int i = 0; i < bytes; i++)
            {
                buffer_data[i] = buffer[i];
            }
            result = new String(buffer_data);
            app.setGet_result(result);
            Message m = handler.obtainMessage();
            Bundle data = new Bundle();
            data.putBoolean("state", true);
            data.putString("function_type", function_type.toString());
            m.setData(data);
            handler.sendMessage(m);
        }catch (Exception e){
            app.setGet_finish(true);
            Message m = handler.obtainMessage();
            Bundle data = new Bundle();
            data.putBoolean("state", false);
            m.setData(data);
            handler.sendMessage(m);
            e.printStackTrace();
        }
    }

}
