package com.example.car;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.adapter.BaseRecyclerAdapter;
import com.adapter.SmartViewHolder;
import com.car.bean.Car;
import com.forum.model.entity.CarDetail;
import com.forum.model.entity.Series;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class SeriesDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Toolbar mToolbar;
    private RefreshLayout mRefreshLayout;
    private static boolean isFirstEnter = true;
    private BaseRecyclerAdapter<Series> mAdapter;
    ArrayList<Series> carseries;
    List<CarDetail> carDetails = MyConstant.carDetails;
    List<Car> cars = MyConstant.cars;
    Intent intent;
    String dicarid;
    int cindex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);
        intent = getIntent();
        dicarid = intent.getStringExtra("dicarid");
        cindex = intent.getIntExtra("index",0);
        mToolbar = findViewById(R.id.toolbar1);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRefreshLayout = findViewById(R.id.refreshLayout);


        View view = findViewById(R.id.recyclerView2);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<Series>(loadModels(), R.layout.item_series_detail) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Series series, int position) {
                    holder.setimage(R.id.imageView3,series.getSeriesimage()+".jpg",10*1024*1024);
                    holder.text(R.id.textView20,series.getSeriesname());
                }
            });

        }
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        dicarid = intent.getStringExtra("dicarid");
        cindex = intent.getIntExtra("index",0);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Series series = mAdapter.get(position);
        Toast.makeText(getApplicationContext(),series.getSeriesname(),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SeriesDetailActivity.this,CarDetailActivity.class);
        intent.putExtra("brand",series.getBrand());
        intent.putExtra("sindex",position);
        intent.putExtra("cindex",cindex);
        startActivity(intent);
    }

    private Collection<Series> loadModels() {
        ArrayList<Series> arrayList = new ArrayList<>();
        // carseries = MyConstant.getCars();
        try {
            for (CarDetail carse : carDetails) {


                arrayList.add(new Series(){
                    {   this.setBrand(cars.get(cindex).getBrand());
                        this.setCname(cars.get(cindex).getName());
                        this.setSeriesid(carse.getCarnum());
                        this.setSeriesname(carse.getBasic_parameter().getModelname());
                        this.setSeriesimage(cars.get(cindex).getPurl());
                    }

                });
            }
        }catch (Exception e){e.printStackTrace();};

        return  arrayList;
    }

}
