package com.example.apple.mybluetootharduino;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends Activity {

    private TextView password1;
    private TextView password2;
    private TextView name;
    private TextView email;
    private TextView telephone;
    private Button button_ok;
    Toast toast;
    Context my_context;
    PostThread post_thread;
    private netResult app;
    private String urlstr = "http://101.5.208.93:5000";
    private netDataHandler postHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        my_context = this;
        app = (netResult) getApplication();
        postHandler = new netDataHandler();
        setContentView(R.layout.activity_register);
        setView();
    }

    public void sendMessage(String name, String password, String email, String telephone){
        List<BasicNameValuePair> request_list = new ArrayList<BasicNameValuePair>();
        request_list.add(new BasicNameValuePair("username", name));
        request_list.add(new BasicNameValuePair("password", password));
        request_list.add(new BasicNameValuePair("email", email));
        request_list.add(new BasicNameValuePair("telephone", telephone));
        post_thread = new PostThread(my_context, String.format("%s/register", urlstr), request_list, (netResult) getApplication(), postHandler);
        post_thread.start();
    }

    public void setView(){
        password1 = (TextView)findViewById(R.id.editText_r_password1);
        password2 = (TextView)findViewById(R.id.editText_r_password2);
        name = (TextView)findViewById(R.id.editText_r_name);
        email = (TextView)findViewById(R.id.editText_r_email);
        telephone = (TextView)findViewById(R.id.editText_r_address);

        button_ok = (Button)findViewById(R.id.button_r_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!password1.getText().toString().equals(password2.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "密码不一致", Toast.LENGTH_SHORT).show();
                    password1.setText("password");
                    password2.setText("password");
                    return;
                }
                sendMessage(name.getText().toString(), password1.getText().toString()
                        , email.getText().toString(), telephone.getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    class netDataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            if(b.getBoolean("state")){
                if(app.getPost_result().substring(0, "The username has already been registered.".length()).equals("The username has already been registered.")){
                    Toast.makeText(getApplicationContext(), "用户名已存在", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    Intent message = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(message);
                }
            }
            super.handleMessage(msg);
        }
    }
}

