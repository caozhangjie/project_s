package com.example.aslan.mybluetootharduino;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private String urlstr;
    private netDataHandler postHandler;
    private static final int REQUEST_ENABLED = 1;
    private TextView password;
    private TextView name;
    private Button button_enter;
    private ImageButton button_head;
    private Button button_register;
    Context my_context;
    PostThread post_thread;
    private netResult app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urlstr = getString(R.string.url_address);
        app = (netResult) getApplication();
        setContentView(R.layout.activity_main);
        my_context = this;
        setView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        postHandler = new netDataHandler();
        return true;
    }

    public void setView(){
        password = (TextView)findViewById(R.id.editText_password);
        name = (TextView)findViewById(R.id.editText_name);
        button_head = (ImageButton)findViewById(R.id.imageButton);
        button_enter = (Button)findViewById(R.id.button_enter);
        button_register = (Button)findViewById(R.id.button_register);
        button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<BasicNameValuePair> request_list = new ArrayList<BasicNameValuePair>();
                request_list.add(new BasicNameValuePair("username", name.getText().toString()));
                request_list.add(new BasicNameValuePair("password", password.getText().toString()));
                post_thread = new PostThread(my_context, String.format("%s/login", urlstr), request_list, (netResult) getApplication(), postHandler, BluetoothActivity.function.enter_in);
                post_thread.start();
                /*Intent todo = new Intent(MainActivity.this, TodoActivity.class);
                startActivity(todo);*/
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });

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

    class netDataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            try {
                if (b.getBoolean("state")) {
                    String result = app.getPost_result();
                    int index = result.indexOf('#');

                    if (index != -1) {
                        app.setUserid(Integer.parseInt(result.substring(0, index)));
                        Intent message = new Intent(my_context, TodoActivity.class);
                        startActivity(message);
                    } else if (result.substring(0, "The password is wrong.".length()).equals("The password is wrong.")) {
                        app.setUserid(-1);
                        Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                    } else {
                        app.setUserid(-1);
                        Toast.makeText(getApplicationContext(), "用户名错误", Toast.LENGTH_SHORT).show();
                    }
                }
                super.handleMessage(msg);
            }catch (Exception e){
                Log.e("ee",e.toString());
            }
        }
    }
}

