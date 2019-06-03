package com.example.car;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.adapter.BaseRecyclerAdapter;
import com.adapter.SmartViewHolder;
import com.forum.model.entity.Series;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static android.R.layout.simple_list_item_2;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class SeriesDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Toolbar mToolbar;
    private RefreshLayout mRefreshLayout;
    private static boolean isFirstEnter = true;
    private BaseRecyclerAdapter<Series> mAdapter;
    List<Series> carseries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRefreshLayout = findViewById(R.id.refreshLayout);


        View view = findViewById(R.id.recyclerView);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<Series>(loadModels(), R.layout.item_detail_car) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Series series, int position) {
                    holder.setimage(R.id.imageView2,series.getSeriesimage(),10*1024*1024);
                    holder.text(R.id.textView5,series.getSeriesname());
                }
            });

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
