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

    Context my_context;
    private ListView text_list_view;
    private List<String> text_list = new ArrayList<String>();
    private ArrayAdapter<String> my_adapter;
    private Button send_button;
    private Button disconnect_button;
    private EditText edit_message;


    private BluetoothAdapter my_bluetooth_adapter;
    private BluetoothSocket my_bluetooth_socket;
    private BluetoothDevice my_bluetooth_device;
    private clientThread my_client;
    private readThread my_read;

    private PostThread post_thread;
    private int threshold = 5;

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
                my_read = new readThread();
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
            double final_result;
            String result = new String();
            List<BasicNameValuePair> request_list = new ArrayList<BasicNameValuePair>();
            int number = 0; //统计收到的温度个数
            while (flag) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
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
                                    result = result.substring(0, result.lastIndexOf('\r'));
                                    final_result = new Double(result);
                                    jsonArray.put(final_result);
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
                    }
                    Log.e(Double.toString(jsonArray.getDouble(0)),jsonArray.toString());
                    jsonObject.put("temp", jsonArray);
                    jsonObject.put("userinfo", userid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                request_list.add(new BasicNameValuePair("temperature", jsonObject.toString()));
                post_thread = new PostThread(my_context, "http://59.66.138.22:5000/save/temperature", request_list, (netResult)getApplication());
                post_thread.start();
                request_list.clear();
                number = 0;
            }
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
