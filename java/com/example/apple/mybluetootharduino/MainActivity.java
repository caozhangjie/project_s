package com.example.apple.mybluetootharduino;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class MainActivity extends Activity implements OnItemClickListener {

    Context this_context;
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
    String urlstr = "http://59.66.138.24:5000";
    private Button net_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this_context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        setBluetooth();
    }

    private void setView(){
        device_list_view = (ListView)findViewById(R.id.device_list);
        list_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, device_list);
        device_list_view.setAdapter(list_adapter);
        device_list_view.setOnItemClickListener(this);
        net_button = (Button) findViewById(R.id.connect_button);
        net_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<BasicNameValuePair> request_list = new ArrayList<BasicNameValuePair>();
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                try {
                    JSONObject userid = new JSONObject();
                    userid.put("userid", 1);
                    jsonArray.put(7.8);
                    jsonArray.put(38.6);
                    jsonObject.put("temp", jsonArray);
                    jsonObject.put("userinfo", userid);
                } catch (JSONException e) {
                    Log.e("error", e.toString());
                    e.printStackTrace();
                }
                request_list.add(new BasicNameValuePair("temperature", jsonObject.toString()));
                post_thread = new PostThread(this_context, "http://59.66.138.22:5000/save/temperature", request_list, (netResult)getApplication());
                post_thread.start();
                while (true)
                {

                    if(((netResult) getApplication()).isPost_finish()){
                        Log.e("result: ", ((netResult) getApplication()).getPost_result());
                        break;
                    }
                }
                /*List<BasicNameValuePair> request_list = new ArrayList<BasicNameValuePair>();
                request_list.add(new BasicNameValuePair("username", "caozhangjie"));
                request_list.add(new BasicNameValuePair("password", "qazwsx"));
                post_thread = new PostThread(this_context, "http://59.66.138.34:5000/register", request_list);
                post_thread.start();*/
                /*get_thread = new GetThread(this_context, "http://59.66.138.22:5000/get/temperature/1", (netResult)getApplication());
                get_thread.start();
                while (true)
                {
                    if(((netResult) getApplication()).isGet_finish()){
                        Log.e("result: ", ((netResult) getApplication()).getGet_result());
                        break;
                    }
                }*/
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        final String device = device_list.get(pos);
        if(my_bluetooth_adapter != null && my_bluetooth_adapter.isDiscovering()){
            my_bluetooth_adapter.cancelDiscovery();
            button_search.setText(R.string.start_search);
        }
        if(device != null) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Comfirmed connection");
            dialog.setMessage(device);
            dialog.setPositiveButton("connect",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyBluetoothDevice.bluetooth_Mac = device.substring(device.length() - 17);
                            Intent transfer = new Intent(MainActivity.this, BluetoothActivity.class);
                            startActivity(transfer);
                        }
                    });
            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MyBluetoothDevice.bluetooth_Mac = null;
                }
            });
            dialog.show();
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
}
