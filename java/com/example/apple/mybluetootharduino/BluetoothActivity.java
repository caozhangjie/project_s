package com.example.apple.mybluetootharduino;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private String urlstr = "http://101.5.208.93:5000";
    private BluetoothAdapter my_bluetooth_adapter;
    private BluetoothSocket my_bluetooth_socket;
    private BluetoothDevice my_bluetooth_device;
    private clientThread my_client;
    private readThread my_read;
    private netDataHandler postHandler;
    private bluetoothDataHandler controlHandler;
    private calculateHandler footLanguageHandler;
    private int my_userid;
    private netResult app;

    private PostThread post_thread;
    private int threshold = 4;

    private DictionaryOpenHelper my_helper = new DictionaryOpenHelper(this);
    private SQLiteDatabase my_database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        my_userid = ((netResult) getApplication()).getUserid();
        setContentView(R.layout.activity_bluetooth);
        my_context = this;
        postHandler = new netDataHandler();
        controlHandler = new bluetoothDataHandler();
        footLanguageHandler = new calculateHandler();
        app = (netResult) getApplication();
        setView();
        setBluetooth();
    }

    @Override
    protected void onResume() {
        my_client = new clientThread(controlHandler);
        my_client.start();
        super.onResume();
    }

    private void setBluetooth() {
        my_bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();
        my_bluetooth_device = my_bluetooth_adapter.getRemoteDevice(MyBluetoothDevice.bluetooth_Mac);
    }

    private class clientThread extends Thread{
        private bluetoothDataHandler handler;
        public clientThread(bluetoothDataHandler h){
            handler = h;
        }
        @Override
        public void run(){
            Message m = handler.obtainMessage();
            Bundle data = new Bundle();
                try {
                    my_bluetooth_socket = my_bluetooth_device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                if(!my_bluetooth_socket.isConnected()) {
                    my_bluetooth_socket.connect();
                    my_read = new readThread(data_class.force, footLanguageHandler);
                    my_read.start();
                }
                    while(true) {
                        if(!my_bluetooth_socket.isConnected()) {
                            my_read.interrupt();
                            my_bluetooth_socket.connect();
                            my_read = new readThread(data_class.force, footLanguageHandler);
                            my_read.start();
                        }
                    }
                } catch (IOException connectException) {
                    try {
                        my_bluetooth_socket.close();
                    }
                    catch (IOException closeException) {
                    }
                    data.putBoolean("state", true);
                    m.setData(data);
                    handler.sendMessage(m);
                }
            }
    }

    protected class readThread extends Thread{
        private data_class data_type;
        private calculateHandler calculateHandler;
        public readThread(data_class data_, calculateHandler h){
            super();
            data_type = data_;
            calculateHandler = h;
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
                //e.printStackTrace();
            }
            double [] data_to_foot_language = new double[15];
            String result = new String();
            String res = new String();
            String temp_str;
            List<BasicNameValuePair> request_list = new ArrayList<BasicNameValuePair>();
            int number = 0; //统计收到的温度个数
            while (flag) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray;
                List<JSONArray> force_tuple = new ArrayList<JSONArray>(5);
                for(int i = 0; i < 5; i++){
                    force_tuple.add(new JSONArray());
                }
                try {
                    JSONObject userid = new JSONObject();
                    userid.put("userid", my_userid);
                    while (number < threshold) {
                        try {
                            // Read from the InputStream
                            if(!my_bluetooth_socket.isConnected()){
                                break;
                            }
                            if ((bytes = my_inputstream.read(buffer)) > 0) {
                                byte[] buf_data = new byte[bytes];
                                for (int i = 0; i < bytes; i++) {
                                    buf_data[i] = buffer[i];
                                }
                                String msg = new String(buf_data);
                                result = result + msg;
                                if (buf_data[bytes - 1] == '\n') {
                                    int len = result.length();
                                    if (len == 0 || result.charAt(0) < '0' || result.charAt(0) > '4') {
                                        result = "";
                                        continue;
                                    }
                                    result = result.substring(0, result.lastIndexOf('\r'));
                                    temp_str = new String(result);
                                    int temp_index = result.indexOf('#');
                                    int pre_temp_index = 0;
                                    while(pre_temp_index != -1) {
                                        jsonArray = new JSONArray();
                                        if(pre_temp_index == 0){
                                            pre_temp_index = -1;
                                        }
                                        if(temp_index == -1){
                                            result = temp_str.substring(pre_temp_index + 1);
                                        }
                                        else {
                                            result = temp_str.substring(pre_temp_index + 1, temp_index);
                                        }
                                        switch (result.charAt(0)) {
                                            case '0':
                                                data_type = data_class.temperature;
                                                break;
                                            case '1':
                                                data_type = data_class.heartrate;
                                                break;
                                            case '2':
                                                data_type = data_class.force;
                                                break;
                                            case '3':
                                                data_type = data_class.accelerator;
                                                break;
                                            case '4':
                                                data_type = data_class.gyro;
                                                break;
                                        }
                                        switch (data_type) {
                                            case temperature:
                                                prepareDataDouble(result.substring(1), jsonArray, force_tuple.get(0));
                                                my_helper.temperatureAdd(my_userid, jsonArray.getDouble(0));
                                                break;
                                            case accelerator:
                                                prepareDataDouble(result.substring(1), jsonArray, force_tuple.get(1));
                                                my_helper.acceleratorAdd(my_userid, jsonArray.getDouble(0), jsonArray.getDouble(1), jsonArray.getDouble(2));
                                                for(int i = 6; i < 9; i++) {
                                                    data_to_foot_language[i] = jsonArray.getDouble(i - 6);
                                                }
                                                break;
                                            case gyro:
                                                prepareDataDouble(result.substring(1), jsonArray, force_tuple.get(2));
                                                my_helper.gyroAdd(my_userid, jsonArray.getDouble(0), jsonArray.getDouble(1), jsonArray.getDouble(2), jsonArray.getDouble(3), jsonArray.getDouble(4), jsonArray.getDouble(5), jsonArray.getDouble(6));
                                                for(int i = 9; i < 15; i++) {
                                                    data_to_foot_language[i] = jsonArray.getDouble(i - 8);
                                                }
                                                break;
                                            case force:
                                                prepareDataInt(result.substring(1), jsonArray, force_tuple.get(3));
                                                my_helper.forceAdd(my_userid, jsonArray.getInt(0), jsonArray.getInt(1), jsonArray.getInt(2), jsonArray.getInt(3), jsonArray.getInt(4), jsonArray.getInt(5));
                                                for(int i = 0; i < 6; i++) {
                                                    data_to_foot_language[i] = jsonArray.getDouble(i); //最后用处理过的数据直接作为力
                                                }
                                                break;
                                            case heartrate:
                                                prepareDataInt(result.substring(1), jsonArray, force_tuple.get(4));
                                                my_helper.heartrateAdd(my_userid, jsonArray.getInt(0));
                                                break;
                                        }
                                        pre_temp_index = temp_index;
                                        temp_index = temp_str.indexOf('#', pre_temp_index + 1);
                                    }
                                    Message hanmsg = new Message();
                                    Bundle data = new Bundle();
                                    data.putDoubleArray("new_data", data_to_foot_language);
                                    hanmsg.setData(data);
                                    calculateHandler.sendMessage(hanmsg);
                                    result = "";
                                    number++;
                                }
                            }
                        } catch (IOException e) {
                            try {
                                my_inputstream.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        } catch (IndexOutOfBoundsException e) {

                        }
                    }
                    if(!my_bluetooth_socket.isConnected()){
                        break;
                    }
                    jsonObject.put("userinfo", userid);
                    jsonObject.put("temp", force_tuple.get(0));
                    jsonObject.put("acc", force_tuple.get(1));
                    jsonObject.put("gy", force_tuple.get(2));
                    jsonObject.put("fo", force_tuple.get(3));
                    jsonObject.put("heart", force_tuple.get(4));
                    request_list.add(new BasicNameValuePair("all_data", jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    post_thread = new PostThread(my_context, String.format("%s/save", urlstr), request_list, app, postHandler);
                    post_thread.start();
                    request_list.clear();
                    number = 0;
                }catch (Exception e){
                    Log.e("error", e.toString());
                }
            }
        }
    }

    private void postDataToServer(List<JSONArray> force_tuple){
        JSONObject jsonObject = new JSONObject();
        List<BasicNameValuePair> request_list = new ArrayList<BasicNameValuePair>();
        try{
            JSONObject userid = new JSONObject();
            userid.put("userid", my_userid);
            jsonObject.put("userinfo", userid);
            jsonObject.put("temp", force_tuple.get(0));
            jsonObject.put("acc", force_tuple.get(1));
            jsonObject.put("gy", force_tuple.get(2));
            jsonObject.put("fo", force_tuple.get(3));
            jsonObject.put("heart", force_tuple.get(4));
            request_list.add(new BasicNameValuePair("all_data", jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            post_thread = new PostThread(my_context, String.format("%s/save", urlstr), request_list, app, postHandler);
            post_thread.start();
        }catch (Exception e){
            Log.e("error", e.toString());
        }
    }

    private void prepareDataDouble(String result, JSONArray one, JSONArray all) {
        String result1;
        double final_result;
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
            //e.printStackTrace();
        }
    }

    private void prepareDataInt(String result, JSONArray one, JSONArray all) {
        String result1;
        int final_result;
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
            //e.printStackTrace();
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
            //e.printStackTrace();
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

    class netDataHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            if(b.getBoolean("state")){
                //收到了网络数据，进行操作
            }
            super.handleMessage(msg);
        }
    }

    class bluetoothDataHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            if(b.getBoolean("state")){
                my_client = new clientThread(controlHandler);
                my_client.start();
            }
            super.handleMessage(msg);
        }
    }

    class calculateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            FootLanguage footLanguage = app.getFootLanguage();
            footLanguage.calculate(b.getDoubleArray("new_data"));
            super.handleMessage(msg);
        }
    }
}
