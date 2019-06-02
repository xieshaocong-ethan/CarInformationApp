package com.example.car;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.car.bean.Car;
import com.car.bean.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImgTestActivity extends AppCompatActivity {
    String s = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_img_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView imageView = findViewById(R.id.imageView4);
        setCarImg("https://img-my.csdn.net/uploads/201404/13/1397393290_5765.jpeg",10*1024*1012,imageView);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void setCarImg(String imgPath,int cacheSize,ImageView imageView) {

        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueues(),
                new BitmapLruCache(cacheSize) {});
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.image_placeholder, R.drawable.image_error);//加载时图片，默认图片
        imageLoader.get(imgPath, listener,200,200);
    }
    public void getCarList(String url){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, (String) null,
            new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {//jsonObject为请求返回的Json格式数据
                            List<Car> result;
                            com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(jsonObject.toString());
                            result = JSON.parseArray(jsonArray.toJSONString(), Car.class);
                            result = new ArrayList<>();
                            Log.e("json", "parseResponseData()中解析json出现异常\"");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(LoginActivity.this,volleyError.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testPost");
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }
}
