package com.example.car;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.BaseRecyclerAdapter;
import com.adapter.SmartViewHolder;
import com.car.bean.User;
import com.util.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 餐饮美食
 */
public class CarMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private class Model {
        int imageId;
        int avatarId;
        String name;
        String nickname;

        public String getName() {
            return name;
        }
    }

    private static boolean isFirstEnter = true;
    private BaseRecyclerAdapter<Model> mAdapter;

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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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
            recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<Model>(loadModels(), R.layout.item_practice_repast) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Model model, int position) {
                    holder.text(R.id.name, model.name);
                    holder.text(R.id.nickname, model.nickname);
                    holder.image(R.id.image, model.imageId);
                    holder.image(R.id.avatar, model.avatarId);
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
                                mAdapter.loadMore(loadModels());
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
                    Model modle = mAdapter.get(position);
                    Toast.makeText(getApplicationContext(),modle.name,Toast.LENGTH_LONG).show();
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
        return Arrays.asList(
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
                }});
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.login) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.login) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Intent intent = getIntent();
//        String userid = (String) intent.getSerializableExtra("userid");
//        String email = (String) intent.getSerializableExtra("email");
//        File cropFile = (File) intent.getSerializableExtra("cropFile");
//
//
//        NavigationView navigationView = (NavigationView)findViewById(R.id.main);
////        navigationView.getMenu().removeItem(22);
//        View headerView = navigationView.getHeaderView(0);
//        TextView TextViewuserid = (TextView)headerView.findViewById(R.id.user_id);
//        TextViewuserid.setText(userid);
//        TextView TextViewemial = (TextView)headerView.findViewById(R.id.user_email);
//        TextViewemial.setText(email);
//        ImageView imageView = (ImageView)headerView.findViewById(R.id.imageView);
//        imageView.setImageBitmap(BitmapFactory.decodeFile(cropFile.getAbsolutePath()));
//        navigationView.getMenu().findItem(R.id.login).setVisible(false);
    }
    @Override
    public void onStart() {
        super.onStart();
 //         DataSupport.deleteAll(User.class);
//        User user = new User();
//        user.setUserid("lzk");
//        user.setIdentity("admin");
//        user.setImgPath("dfdf");
//        user.save();
        List<User> users = DataSupport.findAll(User.class);
        if (users.size()>0){
            NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
            if(navigationView.getHeaderCount() > 0) {
                View headerView = navigationView.getHeaderView(0);
                TextView user_id = (TextView) headerView.findViewById(R.id.user_id);
                user_id.setText(users.get(0).getUserid());
                TextView user_email = (TextView)headerView.findViewById(R.id.user_email);
                user_email.setText(users.get(0).getEmail());
                ImageView imageView = (ImageView)headerView.findViewById(R.id.imageView);
                Bitmap b = null;
                b =  BitmapFactory.decodeFile(users.get(0).getImgPath());
                imageView.setImageBitmap(b);
            }
        }
    }

}
