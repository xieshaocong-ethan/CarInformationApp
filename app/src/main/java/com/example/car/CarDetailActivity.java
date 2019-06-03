package com.example.car;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.adapter.BaseRecyclerAdapter;
import com.adapter.SmartViewHolder;
import com.car.bean.Car;
import com.car.bean.CarDetail;
import com.car.bean.User;
import com.forum.model.entity.Detail;
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
    private BaseRecyclerAdapter<Detail> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        View view = findViewById(R.id.recyclerView1);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //recyclerView.setAdapter(new );

            final Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        /*itemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BottomSheetDialog dialog=new BottomSheetDialog(CarDetailActivity.this);
                View dialogView = View.inflate(getBaseContext(), R.layout.activity_car_detail, null);
                RefreshLayout refreshLayout = dialogView.findViewById(R.id.refreshLayout2);
                RecyclerView recyclerView = new RecyclerView(getBaseContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                recyclerView.setAdapter(dialogAdapter);
                refreshLayout.setEnableRefresh(false);
                refreshLayout.setEnableNestedScroll(false);
                refreshLayout.setRefreshContent(recyclerView);
                dialog.setContentView(dialogView);
                dialog.show();
            }
        });*/


    }





}
