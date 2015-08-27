package com.example.apple.mybluetootharduino;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public class BluetoothActivity extends Activity {

    private enum data_class{
        temperature, force, accelerator, gyro, heartrate;
    }

    Context my_context;
    private ListView text_list_view;
    private List<String> text_list = new ArrayList<String>();
    private ArrayAdapter<String> my_adapter;
    private Button send_button;
    private Button disconnect_button;
    private EditText edit_message;
    private TextView text_show;
    private String urlstr = "http://59.66.138.24:5000";
    private BluetoothAdapter my_bluetooth_adapter;
    private BluetoothSocket my_bluetooth_socket;
    private BluetoothDevice my_bluetooth_device;
    private clientThread my_client;
    private readThread my_read;

    private PostThread post_thread;
    private int threshold = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        my_context = this;
        setView();
        setBluetooth();
    }

    private void setBluetooth() {
        my_bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();
        my_bluetooth_device = my_bluetooth_adapter.getRemoteDevice(MyBluetoothDevice.bluetooth_Mac);
        my_client = new clientThread();
        my_client.start();
    }

    private class clientThread extends Thread{
        @Override
        public void run(){
            try{

                my_bluetooth_socket = my_bluetooth_device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                String msg = "正在连接\n";
                text_list.add(msg);
                my_adapter.notifyDataSetChanged();

                my_bluetooth_socket.connect();
                /*msg = "已连接\n";
                text_list.add(msg);
                my_adapter.notifyDataSetChanged();*/
                my_read = new readThread(data_class.force);
                my_read.start();
            }
            catch (IOException connectException){
                try {
                    my_bluetooth_socket.close();
                }
                catch (IOException closeException){
                }
            }
        }
    }

    protected class readThread extends Thread{
        private data_class data_type;
        public readThread(data_class data_){
            super();
            data_type = data_;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            boolean flag = true;
            InputStream my_inputstream = null;
            try {
                my_inputstream = my_bluetooth_socket.getInputStream();
            } catch (IOException e) {
                flag = false;
                e.printStackTrace();
            }
            String result = new String();
            String res = new String();
            List<BasicNameValuePair> request_list = new ArrayList<BasicNameValuePair>();
            int number = 0; //统计收到的温度个数
            while (flag) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                JSONArray force_tuple = new JSONArray();
                try {
                    JSONObject userid = new JSONObject();
                    userid.put("userid", 1);
                    while (number < threshold) {
                        try {
                            // Read from the InputStream
                            if ((bytes = my_inputstream.read(buffer)) > 0) {
                                byte[] buf_data = new byte[bytes];
                                for (int i = 0; i < bytes; i++) {
                                    buf_data[i] = buffer[i];
                                }
                                String msg = new String(buf_data);
                                result = result + msg;
                                if (buf_data[bytes - 1] == '\n') {
                                    int len = result.length();
                                    if (len == 0) {
                                        continue;
                                    }
                                    switch (data_type){
                                        case temperature:
                                        case accelerator:
                                        case gyro:
                                            prepareDataDouble(result, jsonArray, force_tuple);
                                            break;
                                        case force:
                                        case heartrate:
                                            prepareDataInt(result, jsonArray, force_tuple);
                                            break;
                                    }
                                    result = "";
                                    number++;
                                }
                                /*MyFile file = new MyFile(my_context, "temp.txt", "temperature");
                                file.openExternalPrivateFileForWrite(false);
                                file.writeDataToFile(buf_data,  0, bytes);*/
                            }
                        } catch (IOException e) {
                            try {
                                my_inputstream.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        } catch (IndexOutOfBoundsException e) {

                        }
                        catch (Exception e){
                            Log.e("error", e.toString());
                            break;
                        }
                    }
                    jsonObject.put("userinfo", userid);
                    switch (data_type){
                        case temperature:
                            res = "temperature";
                            jsonObject.put("temp", force_tuple);
                            break;
                        case force:
                            res = "force";
                            jsonObject.put("fo", force_tuple);
                            break;
                        case accelerator:
                            res = "accelerator";
                            jsonObject.put("acc", force_tuple);
                            break;
                        case gyro:
                            res = "gyro";
                            jsonObject.put("gy", force_tuple);
                            break;
                        case heartrate:
                            res = "heartrate";
                            jsonObject.put("heart", force_tuple);
                            break;
                    }
                    request_list.add(new BasicNameValuePair(res, jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    post_thread = new PostThread(my_context, String.format("%s/save/%s", urlstr, res), request_list, (netResult) getApplication());
                    post_thread.start();
                    request_list.clear();
                    number = 0;
                }catch (Exception e){
                    Log.e("error", e.toString());
                }
            }
        }
    }


    private void prepareDataDouble(String result, JSONArray one, JSONArray all) {
        String result1;
        double final_result;
        if(result.lastIndexOf('\r') == -1)
            return;
        result = result.substring(0, result.lastIndexOf('\r'));
        int index = result.indexOf(' ');
        int index1 = -1;
        int i = 0;
        try {
            while(index != -1) {
                result1 = result.substring(index1 + 1, index);
                index1 = index;
                index = result.indexOf(' ', index + 1);
                final_result = new Double(result1);
                one.put(i, final_result);
                i ++;
            }
            result1 = result.substring(index1 + 1);
            final_result = new Double(result1);
            one.put(i, final_result);
            all.put(one);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareDataInt(String result, JSONArray one, JSONArray all) {
        String result1;
        int final_result;
        if(result.lastIndexOf('\r') == -1)
            return;
        result = result.substring(0, result.lastIndexOf('\r'));
        int index = result.indexOf(' ');
        int index1 = -1;
        int i = 0;
        try {
            while(index != -1) {
                result1 = result.substring(index1 + 1, index);
                index1 = index;
                index = result.indexOf(' ', index + 1);
                final_result = new Integer(result1);
                one.put(i, final_result);
                i ++;
            }
            result1 = result.substring(index1 + 1);
            final_result = new Integer(result1);
            one.put(i, final_result);
            all.put(one);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setView(){
        send_button = (Button) findViewById(R.id.send_button);
        disconnect_button = (Button) findViewById(R.id.disconnect_button);
        edit_message = (EditText) findViewById(R.id.text_edit);
        text_list_view = (ListView) findViewById(R.id.device_list);
        my_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, text_list);
        text_list_view.setAdapter(my_adapter);
        edit_message.clearFocus();
        text_show = (TextView) findViewById(R.id.text_show);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edit_message.getText().toString();
                if (msg.length() > 0) {
                    sendMessage(msg);
                    edit_message.setText("");
                    edit_message.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_message.getWindowToken(), 0);
                } else {
                    Toast.makeText(my_context, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        disconnect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void sendMessage(String msg) {
        if(my_bluetooth_socket == null){
            Toast.makeText(my_context, "未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            OutputStream os = my_bluetooth_socket.getOutputStream();
            os.write(msg.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
