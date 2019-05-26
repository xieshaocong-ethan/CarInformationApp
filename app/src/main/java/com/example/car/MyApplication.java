package com.example.car;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.car.bean.User;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.util.DynamicTimeFormat;

/**
 * Created by caobotao on 15/12/23.
 */
public class MyApplication extends org.litepal.LitePalApplication{
    private static RequestQueue queues ;
    static {
        //启用矢量图兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置（优先级最低）
                layout.setEnableAutoLoadMore(true);
                layout.setEnableOverScrollDrag(false);
                layout.setEnableOverScrollBounce(true);
                layout.setEnableLoadMoreWhenContentNotFull(true);
                layout.setEnableScrollContentWhenRefreshed(true);
            }
        });
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);

                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        queues = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getHttpQueues() {
        return queues;
    }
}
