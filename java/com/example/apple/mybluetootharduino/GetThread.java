package com.example.apple.mybluetootharduino;

import android.content.Context;

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
    private String urlstr = "http://59.66.138.24:5000/login";
    private Context this_context;
    private String result;
    private netResult app;

    public GetThread(Context con, String url, netResult a){
        super();
        this_context = con;
        urlstr = url;
        app = a;
    }

    public GetThread(Context con){
        super();
        this_context = con;
    }

    @Override
    public void run(){
        app.setGet_finish(false);
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
        /*MyFile file = new MyFile(this_context, "temp.txt", "temperature");
        file.openExternalPrivateFileForRead();
        file.readDatafromFile(buffer, 0, bytes);*/
        }catch (Exception e){
            app.setGet_finish(true);
            e.printStackTrace();
        }
    }

}
