package com.example.car;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.BaseRecyclerAdapter;
import com.adapter.SmartViewHolder;
import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.car.bean.Car;
import com.car.bean.User;
import com.explore.MainActivity;
import com.forum.model.entity.Model;
import com.forum.ui.activity.ForumActivity;
import com.google.gson.JsonArray;
import com.just.agentweb.AgentWeb;
import com.util.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 餐饮美食
 */


//接口地址：http://localhost:18080/carServer/listCar
public class CarMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    List<Car> cars;
    ArrayList<Car> carArrayList;


    private static boolean isFirstEnter = true;
    private BaseRecyclerAdapter<Car> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        if(navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            ImageView imageView = (ImageView)headerView.findViewById(R.id.imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> users = DataSupport.findAll(User.class);
                if (users.size()>0) {
                    Intent intent = new Intent(CarMainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(CarMainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableFooterFollowWhenNoMoreData(true);

        //第一次进入演示刷新
        if (isFirstEnter) {
            isFirstEnter = false;
            refreshLayout.autoRefresh();
        }

        //初始化列表和监听
        View view = findViewById(R.id.recyclerView);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            cars = MyConstant.cars;
            ArrayList<Car> carArrayList = new ArrayList<>(
            );
            for (int iiii= 0;iiii<cars.size();iiii++) { carArrayList.add(cars.get(iiii));
            }
            recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<Car>(carArrayList,R.layout.item_practice_repast) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Car car, int position) {
                    holder.text(R.id.name, car.getName());
                    holder.text(R.id.nickname, car.getBrand());
                    holder.setimage(R.id.image,car.getPurl()+".jpg",10*1024*1024);
                    holder.setimage(R.id.avatar,car.getBrandPurl()+".png",10*1024*1024);
                }
            }
            );

            refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.finishRefresh();
                            refreshLayout.resetNoMoreData();//setNoMoreData(false);//恢复上拉状态
                        }
                    }, 2000);
                }
                @Override
                public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mAdapter.getCount() > 12) {
                                Toast.makeText(getBaseContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                                refreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
                            } else {
                                //mAdapter.loadMore(loadModels());
                                refreshLayout.finishLoadMore();
                            }
                        }
                    }, 1000);
                }
            });

            refreshLayout.getLayout().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setHeaderInsetStart(DensityUtil.px2dp(toolbar.getHeight()));
                }
            }, 500);
            mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Car car = mAdapter.get(position);
                    Toast.makeText(getApplicationContext(),car.getName(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CarMainActivity.this,SeriesDetailActivity.class);
                    intent.putExtra("dicarid", car.getCarid());
                    intent.putExtra("index",cars.indexOf(mAdapter.get(position)));
                    startActivity(intent);

                }
            });
        }

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this);
        StatusBarUtil.setPaddingSmart(this, view);
        StatusBarUtil.setPaddingSmart(this, toolbar);
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.blurView));

    }

    /**
     * 模拟数据
     */
    private Collection<Model> loadModels() {
        ArrayList<Model> arrayList = new ArrayList<>();
        cars = MyConstant.getCars();
        try {
            for (Car car : cars) {


                arrayList.add(new Model(){
                    {   this.setName(car.getName());
                        this.setNickname(car.getBrand());;
                        this.setImage(car.getPurl()+".jpg");
                        this.setCarid(car.getCarid());
                    }

                });
            }
        }catch (Exception e){e.printStackTrace();};

        return  arrayList;
        /*return Arrays.asList(
                new Model() {{
                    this.name = "但家香酥鸭";
                    this.nickname = "爱过那张脸";
                    this.imageId = R.mipmap.image_practice_repast_1;
                    this.avatarId = R.mipmap.image_avatar_1;
                }}, new Model() {{
                    this.name = "香菇蒸鸟蛋";
                    this.nickname = "淑女算个鸟";
                    this.imageId = R.mipmap.image_practice_repast_2;
                    this.avatarId = R.mipmap.image_avatar_2;
                }}, new Model() {{
                    this.name = "花溪牛肉粉";
                    this.nickname = "性感妩媚";
                    this.imageId = R.mipmap.image_practice_repast_3;
                    this.avatarId = R.mipmap.image_avatar_3;
                }}, new Model() {{
                    this.name = "破酥包";
                    this.nickname = "一丝丝纯真";
                    this.imageId = R.mipmap.image_practice_repast_4;
                    this.avatarId = R.mipmap.image_avatar_4;
                }}, new Model() {{
                    this.name = "盐菜饭";
                    this.nickname = "等着你回来";
                    this.imageId = R.mipmap.image_practice_repast_5;
                    this.avatarId = R.mipmap.image_avatar_5;
                }}, new Model() {{
                    this.name = "米豆腐";
                    this.nickname = "宝宝树人";
                    this.imageId = R.mipmap.image_practice_repast_6;
                    this.avatarId = R.mipmap.image_avatar_6;
                }});*/
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.login) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.luntan) {
            Intent intent = new Intent(this, ForumWebActivity.class);
            startActivity(intent);
        } else if (id == R.id.imgTest) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.imgTest) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        //DataSupport.deleteAll(User.class);
        List<User> users = DataSupport.findAll(User.class);
        if (users.size()>0){
            NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
            if(navigationView.getHeaderCount() > 0) {
                View headerView = navigationView.getHeaderView(0);
                TextView user_id = (TextView) headerView.findViewById(R.id.user_id);
                user_id.setText(users.get(0).getUserid());
                TextView user_email = (TextView)headerView.findViewById(R.id.user_email);
                //user_email.setText(users.get(0).getEmail());
                ImageView imageView = (ImageView)headerView.findViewById(R.id.imageView);
                Bitmap b = null;
                b = BitmapFactory.decodeFile(users.get(0).getImgPath());
                imageView.setImageBitmap(b);
            }
        }
    }


    /*
        cacheSize：缓存大小  10*1024*1024
        imgPath:图片地址
        imageView:要设置的view
    *
    * */


}
