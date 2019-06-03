package com.example.car;

import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.car.bean.Car;
import com.car.bean.CarDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/6.
 */

public class MyConstant {

    public static final String PIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CAR";
    public static final String url = "http://192.168.0.105:18080/";
    public static final String carurl = "http://192.168.0.105:18080/listCar";

    public static List<Car> getCars() {
        return cars;
    }
    public static List<CarDetail> carDetails = null;
    static List<Car> cars;

    public static void setCarImg(String imgPath, int cacheSize, ImageView imageView) {

        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueues(),
                new BitmapLruCache(cacheSize) {});
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.image_placeholder, R.drawable.image_error);//加载时图片，默认图片
        if(imageLoader.isCached(MyConstant.url+imgPath,200,200,imageView.getScaleType()) == false){
            imageLoader.get(MyConstant.url+imgPath, listener);
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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {//jsonObject为请求返回的Json格式数据
                    parseCarDetail(jsonObject);
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
    public static List<CarDetail> parseCarDetail(JSONObject jsonObject){
        com.alibaba.fastjson.JSONObject json = JSON.parseObject(jsonObject.toString());
        List<CarDetail> carDetails = null;
        com.alibaba.fastjson.JSONObject jsonPara1 = json.getJSONObject("params1");
        CarDetail carDetail1 = JSON.toJavaObject(jsonPara1,CarDetail.class);
        com.alibaba.fastjson.JSONObject jsonPara2 = (com.alibaba.fastjson.JSONObject) json.getJSONObject("params1");
        CarDetail carDetail2 = JSON.toJavaObject(jsonPara1,CarDetail.class);
        carDetails.add(carDetail1);
        carDetails.add(carDetail2);
        return carDetails;
    }
}
