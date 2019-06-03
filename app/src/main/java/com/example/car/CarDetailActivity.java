package com.example.car;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.adapter.BaseRecyclerAdapter;
import com.adapter.SmartViewHolder;
import com.car.bean.Car;
import com.car.bean.User;
import com.forum.model.entity.Model;
import com.forum.model.entity.Series;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CarDetailActivity extends AppCompatActivity {


    private static boolean isFirstEnter = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        View view = findViewById(R.id.recyclerView1);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            /*recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<Series>(loadModels(), R.layout.item_detail_car) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Series series, int position) {

                }
            });*/

            final Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }




    }





}
