package com.example.car;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.car.bean.Car;
import com.forum.ui.activity.BaseActivity;
import com.forum.ui.activity.ForumActivity;
import com.forum.ui.util.ActivityUtils;
import com.forum.util.HandlerUtils;

import org.json.JSONArray;

import java.util.List;

public class LaunchActivity extends BaseActivity implements Runnable {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        MyConstant.getCarList();
        Intent startIntent = new Intent(this, MyConstant.class);
         startService(startIntent);
        if (!isTaskRoot()) {
            finish();
            return;
        }

        HandlerUtils.handler.postDelayed(this, 1000);
    }

    @Override
    protected void onDestroy() {
        HandlerUtils.handler.removeCallbacks(this);
        super.onDestroy();
    }

    @Override
    public void run() {
        if (ActivityUtils.isAlive(this)) {
            startActivity(new Intent(this, CarMainActivity.class));
            finish();
        }
    }


}
