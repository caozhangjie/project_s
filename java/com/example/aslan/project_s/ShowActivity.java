package com.example.aslan.project_s;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ShowActivity extends Activity {

    Context my_context;
    private netResult app;
    private enum data_class{
        temperature, force, accelerator, gyro, heartrate;
    }
    private String urlstr;
    enum function{
        depict_heat_map, save_data, register , enter_in;
    }
    private netDataHandler postHandler;
    private boolean bool = false;
    private PostThread post_thread;
    private int[][] r,g,b = new int[400][1200];
    public SurfaceView surface_draw;
    public SurfaceHolder surfaceholder;
    Shader mLinearGradient = null;
    private JSONArray forceData = new JSONArray();
    private DictionaryOpenHelper my_helper;
    //private SQLiteDatabase my_database = null;

    private TimerTask timerTask_draw = new TimerTask() {

        @Override
        public void run() {
            android.os.Message message = new android.os.Message();
            message.what = 1;
            myHandler.sendMessage(message);
            bool = true;
        }
    };

    private Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) {
                case 1:
                    try {
                        forceData.put(my_helper.getLastforce1FromForce());
                        forceData.put(my_helper.getLastforce2FromForce());
                        forceData.put(my_helper.getLastforce3FromForce());
                        forceData.put(my_helper.getLastforce4FromForce());
                        forceData.put(my_helper.getLastforce5FromForce());
                        forceData.put(my_helper.getLastforce6FromForce());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("dateputting","......");
                    }
                    if (bool) depict_heat_map(forceData);
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("oncreate", "1");
        urlstr = getString(R.string.url_address);
        setContentView(R.layout.activity_show);
        postHandler = new netDataHandler();
        app = (netResult) getApplication();
        my_helper = app.getHelper();
        setView();
    }

    private void setView(){
        surface_draw = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceholder = surface_draw.getHolder();
        Log.e("1111","2222");
        //new Timer().schedule(timerTask_draw, new Date(), 1000);
        try {
            forceData.put(1000);
            forceData.put(800);
            forceData.put(600);
            forceData.put(900);
            forceData.put(700);
            forceData.put(500);
            Log.e("2222","3333");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dateputting","......");
        }
        depict_heat_map(forceData);
    }

    void ClearDraw() {
        Canvas canvas = surfaceholder.lockCanvas(null);
        canvas.drawColor(Color.BLACK);
        surfaceholder.unlockCanvasAndPost(canvas);
    }

    void DrawAllPoint(int[][] a){
        Log.e("Draw","111");
        int rr = 3;
        Canvas canvas = surfaceholder.lockCanvas(new Rect(0,0,400,1200));
        Paint Paint_point = new Paint();
        float f = 1/2;
        Paint_point.setStrokeWidth(f);
        for (int i = 0 ; i < 99;i++)
            for (int j = 0 ;j < 299;j++){
                mLinearGradient = new LinearGradient(i * rr, j* rr, (i+1)*rr, (j+1)*rr, new int[] {
                        a[i][j], a[i+1][j+1] }, null,
                        Shader.TileMode.CLAMP);
                Paint_point.setShader(mLinearGradient);
                Paint_point.setAlpha(255);
                canvas.drawRect(i * rr, j * rr, (i + 1) * rr, (j + 1) * rr, Paint_point);
                Paint_point.setAlpha(127);
                mLinearGradient = new LinearGradient(i*rr, (j+1)*rr, (i+1)*rr, j*rr, new int[] {
                        a[i][j+1], a[i+1][j] }, null,
                        Shader.TileMode.CLAMP);
                Paint_point.setShader(mLinearGradient);
                canvas.drawRect(i * rr, j * rr, (i + 1) * rr, (j + 1) * rr, Paint_point);
            }

        surfaceholder.unlockCanvasAndPost(canvas);
    }

    void DrawXY(){
        Canvas canvas = surfaceholder
                .lockCanvas(new Rect(0, 0, 1000, 1400));
        Paint Paint_1 = new Paint();
        Paint_1.setColor(Color.WHITE);
        Paint_1.setStrokeWidth(5);
        canvas.drawRect(50, 1350, 50, 1350, Paint_1);
        Paint_1.setStrokeWidth(2);
        canvas.drawLine(50, 50, 50, 1350, Paint_1);
        canvas.drawLine(40, 60, 50, 50, Paint_1);
        canvas.drawLine(50, 50, 60, 60, Paint_1);
        canvas.drawLine(50, 1350, 950, 1350, Paint_1);
        canvas.drawLine(940, 1340, 950, 1350, Paint_1);
        canvas.drawLine(950, 1350, 940, 1360, Paint_1);

        int r = 255;
        int g = 0;
        int b = 0;
        Paint_1.setColor(Color.rgb(r,g,b));
        canvas.drawCircle(500, 500, 200, Paint_1);
        surfaceholder.unlockCanvasAndPost(canvas);

    }

    class netDataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            JSONObject jsonObject;
            Log.e("handler","handler");
            if(b.getBoolean("state")){
                Log.e("handler",b.getString("function_type"));
                if(b.getString("function_type").equals(function.depict_heat_map.toString())){
                    Log.e("handler","handddd");
                    try {
                        Log.e("11",app.getPost_result());
                        Log.e("111",Integer.toString(app.getPost_result().length()));
                        jsonObject = new JSONObject(app.getPost_result());
                        Log.e("1",jsonObject.toString());
                        JSONArray force_matrix = jsonObject.getJSONArray("force_list");
                        int len = force_matrix.length();
                        int len1 = force_matrix.getJSONArray(0).length();
                        int [][] force_data = new int[len][len1];
                        double maxforce=0;
                        for(int i = 0; i < len; i++) {
                            for(int j = 0; j < len1; j++) {
                                force_data[i][j] = force_matrix.getJSONArray(i).getInt(j);
                                if (force_data[i][j] > maxforce ) maxforce = force_data[i][j];
                            }
                        }
                        Log.e("dataa","1");
                        //force_data是已有的25*75的矩阵，在这里插入画图函数或画图代码
                        ClearDraw();
                        DrawAllPoint(force_data);

                    } catch (JSONException e) {
                        Log.e("666","233");
                    }
                }

            }
            super.handleMessage(msg);
        }
    }

    public void depict_heat_map(JSONArray jsonArray) {
        List<BasicNameValuePair> request_list = new ArrayList<BasicNameValuePair>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("force_data", jsonArray);
        } catch (JSONException e) {

        }
        request_list.add(new BasicNameValuePair("all_data", jsonObject.toString()));
        PostThread postThread = new PostThread(my_context, String.format("%s/get/force/heat_map", urlstr), request_list, app, postHandler, BluetoothActivity.function.depict_heat_map);
        postThread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show, menu);
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
