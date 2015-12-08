package com.example.aslan.mybluetootharduino;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
            //´´½¨ÇëÇó²¢ÉèÖÃÊôÐÔ
            HttpEntity request_entity = new UrlEncodedFormEntity(pair_list);
            HttpPost my_post = new HttpPost(urlstr);
            my_post.setEntity(request_entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(my_post);
            HttpEntity response_entity = response.getEntity();
            InputStream in = response_entity.getContent();
            byte[] buffer = new byte[1000000];
            int bytes = in.read(buffer);

            //temporary_Save(buffer , Context.MODE_APPEND);

            result = new String(buffer);
            String result_sum = "";
            if(function_type == BluetoothActivity.function.depict_heat_map) {
                while (buffer[bytes - 1] != '#') {
                    result = new String(buffer);
                    result_sum = result_sum + result.substring(0, bytes);
                    bytes = in.read(buffer);
                    if (bytes <= 0) break;
                }
                result = new String(buffer);
                result_sum = result_sum + result.substring(0, bytes);
                bytes = result_sum.length();
                result_sum = result_sum.substring(0, bytes - 1);
                app.setPost_result(result_sum);
            }
            else {
                app.setPost_result(result);
            }

            Message m = handler.obtainMessage();
            Bundle data = new Bundle();
            data.putBoolean("state", true);
            data.putString("function_type", function_type.toString());
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

    private void temporary_Save(byte[] buffer , int id){

        FileOutputStream out = null;
        try {
            out = this_context.openFileOutput("filename", id);
            out.write(buffer);
            out.close();
        } catch (java.io.IOException e) {
            Log.e("Tempsave failed", "110");
            e.printStackTrace();
        }

    }
}
