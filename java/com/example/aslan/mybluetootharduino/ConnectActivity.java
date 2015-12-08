package com.example.aslan.mybluetootharduino;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class ConnectActivity extends Activity implements OnItemClickListener {

    private enum data_class{
        temperature, force, accelerator, gyro, heartrate;
    }

    private data_class data_type;

    Context this_context;
    private BluetoothDevice my_bluetooth_device;
    private static final int REQUEST_ENABLED = 1;
    private BluetoothAdapter my_bluetooth_adapter = null;
    private List<String> device_list = new ArrayList<String>();
    private ListView device_list_view;
    private Button button_search;
    private ArrayAdapter<String> list_adapter;
    private boolean has_register = false;
    private BluetoothDeviceReceiver bluetooth_receiver;
    private GetThread get_thread;
    private PostThread post_thread;
    private String urlstr;
    private Button net_button;
    private TextView text_show;
    Timer timer;
    TimerTask timerTask;
    private int userid;
    private netDataHandler getHandler;
    private ProgressDialog progressDialog;
    private netResult app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this_context = this;
        userid = ((netResult) getApplication()).getUserid();
        app = (netResult) getApplication();
        super.onCreate(savedInstanceState);
        urlstr = getString(R.string.url_address);
        getHandler = new netDataHandler();
        setContentView(R.layout.activity_connect);
        setView();
        setBluetooth();
    }

    private void setView(){
        device_list_view = (ListView)findViewById(R.id.device_list);
        list_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, device_list);
        device_list_view.setAdapter(list_adapter);
        device_list_view.setOnItemClickListener(this);
        text_show = (TextView) findViewById(R.id.text_show1);
        net_button = (Button) findViewById(R.id.connect_button);
        net_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent show = new Intent(ConnectActivity.this, ShowActivity.class);
                    startActivity(show);
                }
                catch (Exception e){
                    Log.e(e.toString(),"1111");
                }
            }
        });
        button_search = (Button) findViewById(R.id.search_button);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (my_bluetooth_adapter.isDiscovering()) {
                    my_bluetooth_adapter.cancelDiscovery();
                    button_search.setText(R.string.start_search);
                } else {
                    device_list.clear();
                    setBondedDevice();
                    my_bluetooth_adapter.startDiscovery();
                    button_search.setText(R.string.stop_search);
                }
            }
        });
    }

    private void setBluetooth(){
        my_bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();
        if(my_bluetooth_adapter != null){
            if(!my_bluetooth_adapter.isEnabled()) {
                Intent enable_bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enable_bluetooth, REQUEST_ENABLED);
            }
        }
        else{
            finish();
        }
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent data){
        switch (result_code){
            case RESULT_OK:
                Toast.makeText(this, "The bluetooth is open.", Toast.LENGTH_SHORT);
                list_adapter.notifyDataSetChanged();
                break;
            case RESULT_CANCELED:
                Toast.makeText(this, "The bluetooth isn't open.", Toast.LENGTH_SHORT);
                list_adapter.notifyDataSetChanged();
                break;
        }
    }

    private void setBondedDevice(){
        Set<BluetoothDevice> bonded_device = my_bluetooth_adapter.getBondedDevices();
        BluetoothDevice temp_device;
        if(bonded_device.size() > 0){
            for(Iterator<BluetoothDevice> iterator = bonded_device.iterator(); iterator.hasNext();){
                temp_device = iterator.next();
                device_list.add(temp_device.getName() + '\n' + temp_device.getAddress());
                list_adapter.notifyDataSetChanged();
            }
        }
        else{
            device_list.add("No bonded device!");
            list_adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onStart(){
        if(!has_register){
            has_register = true;
            bluetooth_receiver = new BluetoothDeviceReceiver();
            IntentFilter find_device = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            IntentFilter finished_search = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(bluetooth_receiver, find_device);
            registerReceiver(bluetooth_receiver, finished_search);
        }
        super.onStart();
    }

    @Override
    protected void onDestroy(){
        if(has_register) {
            has_register = false;
            unregisterReceiver(bluetooth_receiver);
        }
        if(my_bluetooth_adapter != null && my_bluetooth_adapter.isDiscovering()){
            my_bluetooth_adapter.cancelDiscovery();
        }
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connect, menu);
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

    private Handler progressdialog_handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            //关闭ProgressDialog
            progressDialog.dismiss();

            //更新UI
            if (msg.what == 1) Toast.makeText(this_context,"获取设备失败",Toast.LENGTH_LONG).show();
            if (msg.what == 2) Toast.makeText(this_context,"连接失败",Toast.LENGTH_LONG).show();
            if (msg.what == 3) Toast.makeText(this_context,"连接成功",Toast.LENGTH_LONG).show();
        }};

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        final String device = device_list.get(pos);
        if(my_bluetooth_adapter != null && my_bluetooth_adapter.isDiscovering()){
            my_bluetooth_adapter.cancelDiscovery();
            button_search.setText(R.string.start_search);
        }
        if(device != null) {
            /*AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Connecting");
            dialog.setMessage("Is connecting to the device now");
            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(my_bluetooth_socket != null && !my_bluetooth_socket.isConnected()){
                        try {
                            my_bluetooth_socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            dialog.show();*/

            progressDialog = new ProgressDialog(ConnectActivity.this);
            progressDialog.setTitle("Connecting");
            progressDialog.setMessage(device);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            Log.e("progress","dialog");
            new Thread(){

                @Override
                public void run() {
                    Message msg = new Message();
                    MyBluetoothDevice.bluetooth_Mac = device.substring(device.length() - 17);
                    boolean flag_success = false;
                    my_bluetooth_device = my_bluetooth_adapter.getRemoteDevice(MyBluetoothDevice.bluetooth_Mac);
                    try {
                        app.my_bluetooth_socket = my_bluetooth_device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                        flag_success = true;
                    } catch (IOException e) {

                        msg.what = 1;
                        progressdialog_handler.sendMessage(msg);
                /*dialog.setTitle("获取设备失败");
                dialog.show();*/
                        e.printStackTrace();
                        return;
                    }
                    if (flag_success) {
                        try {
                            app.my_bluetooth_socket.connect();
                        } catch (IOException e) {

                    /*dialog.setTitle("连接失败");
                    dialog.show();*/
                            msg.what = 2;
                            progressdialog_handler.sendMessage(msg);
                            try {
                                app.my_bluetooth_socket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                            return;
                        }
                        msg.what = 3;
                        progressdialog_handler.sendMessage(msg);
                        Intent Bluetooth_intent = new Intent(ConnectActivity.this, BluetoothActivity.class);
                        startActivity(Bluetooth_intent);
                    }
                }}.start();

        }
    }


    private class BluetoothDeviceReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice temp_bluetooth = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(temp_bluetooth.getBondState() != BluetoothDevice.BOND_BONDED){
                    device_list.add(temp_bluetooth.getName() + '\n' + temp_bluetooth.getAddress());
                    list_adapter.notifyDataSetChanged();
                }
            }
            else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                if(device_list.size() <= 0)
                {
                    device_list.add("No more device can be found.");
                    list_adapter.notifyDataSetChanged();
                }
                button_search.setText(R.string.start_search);
            }
        }
    }

    class netDataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            if(b.getBoolean("state")){
                //ÊÕµ½ÁËÍøÂçÊý¾Ý£¬½øÐÐ²Ù×÷
            }
            super.handleMessage(msg);
        }
    }
}
