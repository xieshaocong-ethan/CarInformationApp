package com.example.car;

import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.car.bean.Car;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6.
 */

public class MyConstant {

    public static final String PIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CAR";
    public static final String url = "http://192.168.43.72:18080/";
    public static final String carurl = "http://192.168.43.72:18080/listCar";

    public static List<Car> getCars() {
        return cars;
    }

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
}
