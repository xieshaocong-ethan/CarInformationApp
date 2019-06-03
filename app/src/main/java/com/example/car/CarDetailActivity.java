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

    List<Series> carseries;
    private static boolean isFirstEnter = true;
    private BaseRecyclerAdapter<Series> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        View view = findViewById(R.id.recyclerView1);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<Series>(loadModels(), R.layout.item_detail_car) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Series series, int position) {
                    holder.setimage(R.id.imageView2,series.getSeriesimage(),10*1024*1024);
                    holder.text(R.id.textView5,series.getSeriesname());
                }
            });

            final Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }




    }

    private Collection<Series> loadModels() {
        ArrayList<Series> arrayList = new ArrayList<>();
       // carseries = MyConstant.getCars();
        try {
            for (Series carse : carseries) {


                arrayList.add(new Series(){
                    {   this.setBrand(carse.getBrand());
                        this.setCname(carse.getCname());
                        this.setSeriesid(carse.getSeriesid());
                        this.setSeriesname(carse.getSeriesname());
                        this.setSeriesimage(carse.getSeriesimage());
                    }

                });
            }
        }catch (Exception e){e.printStackTrace();};

        return  arrayList;
    }



}
