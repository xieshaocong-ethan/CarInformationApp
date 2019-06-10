package com.example.car;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;
import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.car.bean.Car;
import com.forum.model.entity.CarDetail;
import com.scwang.smartrefresh.header.material.CircleImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.TrafficStats.getTotalTxBytes;

/**
 * Created by Administrator on 2018/6/6.
 */

public class MyConstant extends Service {

    public static final String PIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CAR";
    public static final String url = "http://192.168.43.72:8080/";
    public static final String carurl = "http://192.168.43.72:8080/listCar";
    public static final String TAG = "LoadData";
    public static final String trafficTAG = "总流量";
    public static double tf =0;

    public static List<Car> getCars() {
        return cars;
    }
    static List<CarDetail> carDetails = null;
    static List<Car> cars ;
    static ArrayList<Car> carArrayList = null;

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        super.onCreate();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        gettrafiicmonitor();

        if (Build.VERSION.SDK_INT >= 28) {
            String channelId = "111";
            String channelName = "Mb";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
            DecimalFormat df = new DecimalFormat("######0.00");
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setChannelId(channelId)
                    .setContentTitle("应用使用流量：")
                    .setContentText(df.format(tf)+"Mb")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            Intent notificationIntent = new Intent(getApplicationContext(), LaunchActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
            startForeground(111,notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void setCarImg(String imgPath, int cacheSize, ImageView imageView) {

        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueues(),
                new BitmapLruCache(cacheSize) {});
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.image_placeholder, R.drawable.image_error);//加载时图片，默认图片
        if(imageLoader.isCached(MyConstant.url+imgPath,200,200,imageView.getScaleType()) == false){
            imageLoader.get(MyConstant.url+imgPath, listener);
        }
    }
    public static void setCarImg(String imgPath, int cacheSize, ImageView imageView, boolean islarge) {

        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueues(),
                new BitmapLruCache(cacheSize) {});
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.image_placeholder, R.drawable.image_error);//加载时图片，默认图片
        if(imageLoader.isCached(MyConstant.url+imgPath,430,430,imageView.getScaleType()) == false){
            imageLoader.get(MyConstant.url+imgPath, listener);
        }
    }
    public static void setCarImg(String imgPath, int cacheSize, CircleImageView imageView) {
        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueues(),
                new BitmapLruCache(cacheSize) {
                });
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.image_placeholder, R.drawable.image_error);//加载时图片，默认图片
        if (imageLoader.isCached(MyConstant.url + imgPath, 200, 200, imageView.getScaleType()) == false) {
            imageLoader.get(MyConstant.url + imgPath, listener);
        }
    }
    public static void getCarList(){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, carurl, (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonObject) {//jsonObject为请求返回的Json格式数据

                        com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(jsonObject.toString());
                        cars = JSON.parseArray(jsonArray.toJSONString(), Car.class);
                        Log.i("connection",jsonObject.toString() );

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("fail",volleyError.toString() );
                        String s = volleyError.toString();
                        System.out.println(s);
                        //Toast.makeText(LaunchActivity,volleyError.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testPost");
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

    public static void getcarDetail(String carid) {
        String url = MyConstant.url+"getCarDitail";
        Map<String,String> map = new HashMap<>();
        map.put("carId",carid);
        //将map转化为JSONObject对象
        JSONObject jsonObject = new JSONObject(map);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {//jsonObject为请求返回的Json格式数据
                        //parseCarDetail(jsonArray);
                        carDetails = JSON.parseArray(jsonArray.toString(),CarDetail.class);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testPost");
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,1));
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

    public void gettrafiicmonitor(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    double total01 = getTotalTxBytes();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    double total02 = getTotalTxBytes();
                    double errorTraffic = total02 - total01;
                    if (errorTraffic < 512) {
                        errorTraffic = 1;
                    }
                    tf += errorTraffic / 1111500;

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Log.d(trafficTAG,String.valueOf(tf));
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @TargetApi(28)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setDescription("流量监控");
        // 该渠道的通知是否使用震动
        channel.enableVibration(true);
        channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    public static List<CarDetail> parseCarDetail(JSONArray jsonArray){
        carDetails = JSON.parseArray(jsonArray.toString(),CarDetail.class);
        return carDetails;
    }
}
