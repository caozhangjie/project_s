package com.example.aslan.mybluetootharduino;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.List;


public class MapActivity extends Activity {
    private BaiduMap.OnMapClickListener mapClickListener;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private Button button_set = null;
    private Button button_now = null;
    private EditText start_place;
    private EditText end_place;
    private RoutePlanSearch mSearch;
    private OnGetRoutePlanResultListener listener;
    private LocationClient mlocation_client;
    //private NotifyLister my_lister;
    private enum trans_method{
        public_traffic, car, walking;
    }
    private Marker marker;
    private Marker marker_now;
    private List<WalkingRouteLine> searched_route_walk;
    private List<DrivingRouteLine> searched_route_drive;
    private List<TransitRouteLine> searched_route_transit;
    private String standard = "gcj02";


    private double math_pi = 3.14159265358979323846265 * 3000.0 /180.0;
    private double now_latitude = 116.328511;
    private double now_longitude = 39.99821;
    private double next_latitude;
    private double next_longitude;
    private boolean start_conduct;
    private int number_of_segment;
    private trans_method method;
    private int now_seg;
    private double threshold;

    private Intent intent;
    private PendingIntent pi;

    Context my_context;
    PostThread post_thread;
    private netResult app;
    private String urlstr;

    private LocationManager manager;
    private Location location;

    private double destination_latitude;
    private double destination_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        my_context = this;


        start_conduct = false;
        IntentFilter filter = new IntentFilter("com.example.aslan.mybluetootharduino.PROXIMITY_ALERT");
        registerReceiver(new ProximityAlertReceiver(), filter);
        SDKInitializer.initialize(getApplicationContext());
        mapClickListener = new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                destination_latitude = latLng.latitude;
                destination_longitude = latLng.longitude;
                LatLng point = new LatLng(destination_latitude, destination_longitude);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_markb);
                //构建MarkerOption，用于在地图上添加Marker
                final MarkerOptions myoption = new MarkerOptions().position(point).icon(bitmap).zIndex(9).draggable(true);
                //在地图上添加Marker，并显示
                marker.remove();
                marker = (Marker)mBaiduMap.addOverlay(myoption);
                Toast.makeText(MapActivity.this, " 目的地经度"+Double.toString(destination_longitude) + "目的地纬度"+ Double.toString(destination_latitude),Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        };
        setContentView(R.layout.activity_map);
        setMap();
        initlizeMarker();
        mBaiduMap.setOnMapClickListener(mapClickListener);
        app = (netResult) getApplication();
        //app.setHandler(new placeHandler());

        //mlocation_client = app.getLocationClient();
        start_place = (EditText)findViewById(R.id.start_place);
        end_place = (EditText) findViewById(R.id.end_place);
        threshold = 1;
        //initLocation();
        intent = new Intent("com.example.aslan.mybluetootharduino.PROXIMITY_ALERT");
        pi = PendingIntent.getBroadcast(this, 0,intent,0);
        manager=(LocationManager)getSystemService(LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, new LocationListener() {

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MapActivity.this, "设备精神不正常", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MapActivity.this, "设备良好", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MapActivity.this, "设备已跪", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        // TODO Auto-generated method stub
                        //location为变化完的新位置，更新显示
                        now_latitude = location.getLatitude();
                        now_longitude = location.getLongitude();
                        double z = Math.sqrt(now_latitude * now_latitude + now_longitude * now_longitude) + 0.00002 * Math.sin(now_latitude * math_pi);
                        double theta = Math.atan2(now_latitude, now_longitude) + 0.000003 * Math.cos(now_longitude * math_pi);
                        now_longitude = z * Math.cos(theta) + 0.012;
                        now_latitude = z * Math.sin(theta) + 0.006;
                        Toast.makeText(MapActivity.this, "定位现在位置成功", Toast.LENGTH_LONG).show();
                        Log.e(Double.toString(now_latitude), Double.toString(now_longitude));
                        LatLng point = new LatLng(now_latitude, now_longitude);
                        //构建Marker图标
                        BitmapDescriptor bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.icon_marka);
                        //构建MarkerOption，用于在地图上添加Marker
                        final MarkerOptions myoption = new MarkerOptions().position(point).icon(bitmap).zIndex(9).draggable(true);
                        //在地图上添加Marker，并显示
                        marker_now.remove();
                        marker_now = (Marker) mBaiduMap.addOverlay(myoption);
                    }
                });
                /*Log.e("new","gps data");
                if(start_conduct) {
                    Log.e("conducting",Double.toString((Math.pow((now_longitude - next_longitude), 2) + Math.pow((now_latitude - next_latitude),2))));
                    if ((Math.pow((now_longitude - next_longitude), 2) + Math.pow((now_latitude - next_latitude), 2)) < threshold) {
                        now_seg++;
                        if (now_seg == number_of_segment) {
                            Toast.makeText(MapActivity.this, "已到目的地", Toast.LENGTH_LONG).show();
                            start_conduct = false;
                            return;
                        }
                        int len;
                        switch (method) {
                            case car:
                                len = searched_route_drive.get(0).getAllStep().get(now_seg).getWayPoints().size();
                                next_latitude = searched_route_drive.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).latitude;
                                next_longitude = searched_route_drive.get(0).getWayPoints().get(now_seg).getLocation().longitude;
                                break;
                            case public_traffic:
                                len = searched_route_transit.get(0).getAllStep().get(now_seg).getWayPoints().size();
                                next_latitude = searched_route_transit.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).latitude;
                                next_longitude = searched_route_transit.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).longitude;
                                break;
                            case walking:
                                len = searched_route_walk.get(0).getAllStep().get(now_seg).getWayPoints().size();
                                next_latitude = searched_route_walk.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).latitude;
                                next_longitude = searched_route_walk.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).longitude;
                                Toast.makeText(MapActivity.this, "行进到第" + now_seg + "路段", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }
            }
        });*/
        //定义使用矢量图还是卫星图
        /*
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //卫星地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //开启交通图
        mBaiduMap.setTrafficEnabled(true);
        //开启交通图
        mBaiduMap.setBaiduHeatMapEnabled(true);
        */
        //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
        //定义Maker坐标点
        /*LatLng point = new LatLng(39.963175, 116.400244);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker
        final MarkerOptions myoption = new MarkerOptions().position(point).icon(bitmap).zIndex(9).draggable(true);
        //在地图上添加Marker，并显示
        marker = (Marker)mBaiduMap.addOverlay(myoption);
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
                //拖拽中
                Log.e("processing", marker.getPosition().toString());
            }
            public void onMarkerDragEnd(Marker marker) {
                //拖拽结束
                Log.e("end", marker.getPosition().toString());
            }
            public void onMarkerDragStart(Marker marker) {
                //开始拖拽
                Log.e("start",marker.getPosition().toString());

            }
        });
        //定义多边形的五个顶点
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.91923, 116.327428);
        LatLng pt3 = new LatLng(39.89923, 116.347428);
        LatLng pt4 = new LatLng(39.89923, 116.367428);
        LatLng pt5 = new LatLng(39.91923, 116.387428);
        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        pts.add(pt5);
//构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(5, 0xAA00FF00))
                .fillColor(0xAAFFFF00);
//在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(polygonOption);
        //定义文字所显示的坐标点
        LatLng llText = new LatLng(39.86923, 116.397428);
//构建文字Option对象，用于在地图上添加文字
        OverlayOptions textOption = new TextOptions()
                .bgColor(0xAAFFFF00)
                .fontSize(24)
                .fontColor(0xFFFF00FF)
                .text("百度地图SDK")
                .rotate(-30)
                .position(llText);
//在地图上添加该文字对象并显示
        mBaiduMap.addOverlay(textOption);*/

        Location lastKnownLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        now_latitude = lastKnownLocation.getLatitude();
        now_longitude = lastKnownLocation.getLongitude();
        button_set = (Button) findViewById(R.id.button_set);
        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_route("北京", "清华大学", "北京", "北京大学", trans_method.walking);
            }
        });
        button_now = (Button) findViewById(R.id.button_now);
        button_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_route_from_now_to_ll(destination_latitude, destination_longitude, trans_method.walking);
            }
        });
    }

    private void initlizeMarker(){
        LatLng point = new LatLng(0, 0);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_markb);
        //构建MarkerOption，用于在地图上添加Marker
        final MarkerOptions myoption = new MarkerOptions().position(point).icon(bitmap).zIndex(9).draggable(true);
        //在地图上添加Marker，并显示
        marker = (Marker)mBaiduMap.addOverlay(myoption);
    }

    /*public class ChangeDirectionListener implements BDLocationListener {
        ChangeDirectionListener()
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            longtitude = location.getLongitude();
            latitude = location.getLatitude();
            notifyHandler.sendEmptyMessage(0);
        }
    }*/

    /*public class NotifyLister extends BDNotifyListener {
        private int now_seg;
        private int all_seg;
        private trans_method method;
        NotifyLister(int number_of_seg, trans_method m){
            all_seg = number_of_seg;
            method = m;
            now_seg = 0;
        }
        public void onNotify(BDLocation mlocation, float distance){
            Toast.makeText(MapActivity.this, "震动提醒", Toast.LENGTH_LONG).show();
            Log.e("eee","rrr");
            now_seg++;
            if(now_seg == all_seg){
                Toast.makeText(MapActivity.this, "已到目的地", Toast.LENGTH_LONG).show();
                mlocation_client.stop();
                mlocation_client.removeNotifyEvent(my_lister);
                mlocation_client.start();
                return;
            }
            mlocation_client.removeNotifyEvent(my_lister);
            int len;
            switch (method){
                case car:
                    len = searched_route_drive.get(0).getAllStep().get(now_seg).getWayPoints().size();
                    my_lister.SetNotifyLocation(searched_route_drive.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).latitude,
                            searched_route_drive.get(0).getWayPoints().get(now_seg).getLocation().longitude, 100, standard);
                    break;
                case public_traffic:
                    len = searched_route_transit.get(0).getAllStep().get(0).getWayPoints().size();
                    my_lister.SetNotifyLocation(searched_route_transit.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).latitude,
                            searched_route_transit.get(0).getAllStep().get(0).getWayPoints().get(len - 1).longitude, 100, standard);
                    break;
                case walking:
                    len = searched_route_walk.get(0).getAllStep().get(0).getWayPoints().size();
                    my_lister.SetNotifyLocation(searched_route_walk.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).latitude,
                            searched_route_walk.get(0).getAllStep().get(0).getWayPoints().get(len - 1).longitude, 100, standard);
                    break;
            }
            mlocation_client.registerNotify(my_lister);
        }
    }*/

    private void setMap(){
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mSearch  =  RoutePlanSearch.newInstance();
        LatLng point = new LatLng(now_latitude, now_longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker
        final MarkerOptions myoption = new MarkerOptions().position(point).icon(bitmap).zIndex(9).draggable(true);
        //在地图上添加Marker，并显示
        marker_now = (Marker) mBaiduMap.addOverlay(myoption);
        listener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                if(walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR){
                    Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
                }
                if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //walkingRouteResult.getSuggestAddrInfo();
                    return;
                }
                if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                    searched_route_walk = walkingRouteResult.getRouteLines();
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(walkingRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    //try {
                    int len = searched_route_walk.get(0).getAllStep().get(0).getWayPoints().size();
                    number_of_segment = searched_route_walk.get(0).getAllStep().size();
                        /*my_lister = new NotifyLister(len, trans_method.walking);
                        my_lister.SetNotifyLocation(searched_route_walk.get(0).getAllStep().get(0).getWayPoints().get(0).latitude,
                                searched_route_walk.get(0).getAllStep().get(0).getWayPoints().get(0).longitude, 100, standard);
                        mlocation_client.registerNotify(my_lister);
                        mlocation_client.start();
                    }catch (Exception e){
                        Log.e("error", e.toString());
                    }*/
                    next_latitude = searched_route_walk.get(0).getAllStep().get(0).getWayPoints().get(len - 1).latitude;
                    next_longitude = searched_route_walk.get(0).getAllStep().get(0).getWayPoints().get(len - 1).longitude;
                    manager.addProximityAlert(next_latitude, next_longitude, 500f, -1, pi);
                    Log.e(Double.toString(now_latitude) + Double.toString(now_longitude), Double.toString(next_latitude) + Double.toString(next_longitude));
                    now_seg = 0;
                    method = trans_method.walking;
                    start_conduct = true;
                }
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                if(transitRouteResult == null || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR){
                    Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
                }
                if (transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //transitRouteResult.getSuggestAddrInfo();
                    return;
                }
                if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
                    searched_route_transit = transitRouteResult.getRouteLines();
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(transitRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    int len = searched_route_transit.get(0).getAllStep().get(0).getWayPoints().size();
                    number_of_segment = searched_route_transit.get(0).getAllStep().size();
                    /*my_lister = new NotifyLister(len, trans_method.public_traffic);
                    my_lister.SetNotifyLocation(searched_route_transit.get(0).getAllStep().get(0).getWayPoints().get(len - 1).latitude,
                            searched_route_transit.get(0).getAllStep().get(0).getWayPoints().get(len - 1).longitude, 100, standard);
                    mlocation_client.registerNotify(my_lister);
                    mlocation_client.start();*/
                    next_latitude = searched_route_transit.get(0).getAllStep().get(0).getWayPoints().get(len - 1).latitude;
                    next_longitude = searched_route_transit.get(0).getAllStep().get(0).getWayPoints().get(len - 1).longitude;

                    now_seg = 0;
                    method = trans_method.public_traffic;
                    start_conduct = true;
                }
            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                if(drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR){
                    Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
                }
                if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //drivingRouteResult.getSuggestAddrInfo();
                    return;
                }
                if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
                    searched_route_drive = drivingRouteResult.getRouteLines();
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(drivingRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    int len = searched_route_drive.get(0).getAllStep().get(0).getWayPoints().size();
                    number_of_segment = searched_route_drive.get(0).getAllStep().size();
                    /*my_lister = new NotifyLister(len, trans_method.car);
                    my_lister.SetNotifyLocation(searched_route_drive.get(0).getAllStep().get(0).getWayPoints().get(len - 1).latitude,
                            searched_route_drive.get(0).getAllStep().get(0).getWayPoints().get(len - 1).longitude, 100, standard);
                    mlocation_client.registerNotify(my_lister);
                    mlocation_client.start();*/
                    next_latitude = searched_route_drive.get(0).getAllStep().get(0).getWayPoints().get(len - 1).latitude;
                    next_longitude = searched_route_drive.get(0).getAllStep().get(0).getWayPoints().get(len - 1).longitude;
                    now_seg = 0;
                    method = trans_method.car;
                    start_conduct = true;
                }
            }
        };
        mSearch.setOnGetRoutePlanResultListener(listener);
    }

    private void search_route(String start_city, String start, String end_city, String end, trans_method m){
        PlanNode stNode = PlanNode.withCityNameAndPlaceName(start_city, start);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(end_city, end);
        switch (m){
            case public_traffic:
                mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode).city(start_city).to(enNode));
                break;
            case car:
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
                break;
            case walking:
                mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
                break;
        }
    }

    /*private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        int span=2000;
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setCoorType(standard);
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mlocation_client.setLocOption(option);
    }*/



    private void search_route_from_now(String end_city, String end, trans_method m){
        PlanNode stNode = PlanNode.withLocation(new LatLng(now_latitude,now_longitude));
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(end_city, end);
        switch (m){
            case public_traffic:
                mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode).city(end_city).to(enNode));
                break;
            case car:
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
                break;
            case walking:
                mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
                break;
        }
    }

    private void search_route_from_now_to_ll(double latitude, double longitude, trans_method m){
        PlanNode stNode = PlanNode.withLocation(new LatLng(now_latitude,now_longitude));
        Log.e(Double.toString(now_longitude), Double.toString(now_latitude));
        PlanNode enNode = PlanNode.withLocation(new LatLng(latitude, longitude));
        switch (m){
            case public_traffic:
                mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode).city("北京").to(enNode));
                break;
            case car:
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
                break;
            case walking:
                mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mSearch.destroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    /*class placeHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            Bundle data = msg.getData();
            Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
            Toast.makeText(MapActivity.this, Double.toString(data.getDouble("latitude")), Toast.LENGTH_LONG).show();
            Toast.makeText(MapActivity.this, Double.toString(data.getDouble("longitude")), Toast.LENGTH_LONG).show();
        }
    }*/

    public class ProximityAlertReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取是否进入指定区域
            boolean isEnter = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
            if (isEnter) {
                // 给出提示信息
                Toast.makeText(context, "enter", Toast.LENGTH_LONG).show();
                now_seg++;
                if(start_conduct) {
                    Toast.makeText(context, "conducting" + Double.toString((Math.pow((now_longitude - next_longitude), 2) + Math.pow((now_latitude - next_latitude), 2))), Toast.LENGTH_LONG).show();
                    if ((Math.pow((now_longitude - next_longitude), 2) + Math.pow((now_latitude - next_latitude), 2)) < threshold) {
                        now_seg++;
                        if (now_seg == number_of_segment) {
                            Toast.makeText(MapActivity.this, "已到目的地", Toast.LENGTH_LONG).show();
                            start_conduct = false;
                            return;
                        }
                        int len;
                        switch (method) {
                            case car:
                                len = searched_route_drive.get(0).getAllStep().get(now_seg).getWayPoints().size();
                                next_latitude = searched_route_drive.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).latitude;
                                next_longitude = searched_route_drive.get(0).getWayPoints().get(now_seg).getLocation().longitude;
                                break;
                            case public_traffic:
                                len = searched_route_transit.get(0).getAllStep().get(now_seg).getWayPoints().size();
                                next_latitude = searched_route_transit.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).latitude;
                                next_longitude = searched_route_transit.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).longitude;
                                break;
                            case walking:
                                len = searched_route_walk.get(0).getAllStep().get(now_seg).getWayPoints().size();
                                next_latitude = searched_route_walk.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).latitude;
                                next_longitude = searched_route_walk.get(0).getAllStep().get(now_seg).getWayPoints().get(len - 1).longitude;
                                Toast.makeText(MapActivity.this, "行进到第" + now_seg + "路段", Toast.LENGTH_LONG).show();
                                manager.removeProximityAlert(pi);
                                manager.addProximityAlert(next_latitude, next_longitude, 500f, -1, pi);
                                break;
                        }
                    }
                }
            } else {
                // 给出提示信息
                Toast.makeText(context, "out", Toast.LENGTH_LONG).show();
            }
        }
    }



    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
