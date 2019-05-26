package com.example.car;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.BaseRecyclerAdapter;
import com.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.util.StatusBarUtil;

import java.util.Arrays;
import java.util.Collection;

public class MainFragment extends Fragment {

    private class Model {
        int imageId;
        int avatarId;
        String name;
        String nickname;
    }

    private static boolean isFirstEnter = true;
    private BaseRecyclerAdapter<MainFragment.Model> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_car_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        final RefreshLayout refreshLayout = getActivity().findViewById(R.id.refreshLayout);
        refreshLayout.setEnableFooterFollowWhenNoMoreData(true);

        //第一次进入演示刷新
        if (isFirstEnter) {
            isFirstEnter = false;
            refreshLayout.autoRefresh();
        }

        //初始化列表和监听
        View view = getActivity().findViewById(R.id.recyclerView);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<MainFragment.Model>(loadModels(), R.layout.item_practice_repast) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, MainFragment.Model model, int position) {
                    holder.text(R.id.name, model.name);
                    holder.text(R.id.nickname, model.nickname);
                    holder.image(R.id.image, model.imageId);
                    holder.image(R.id.avatar, model.avatarId);
                }
            });

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
                                Toast.makeText(getContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
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
        }

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(getActivity());
        StatusBarUtil.setPaddingSmart(getActivity(), view);
        StatusBarUtil.setPaddingSmart(getActivity(), toolbar);
        StatusBarUtil.setPaddingSmart(getActivity(), getActivity().findViewById(R.id.blurView));

    }

    /**
     * 模拟数据
     */
    private Collection<MainFragment.Model> loadModels() {
        return Arrays.asList(
                new MainFragment.Model() {{
                    this.name = "但家香酥鸭";
                    this.nickname = "爱过那张脸";
                    this.imageId = R.mipmap.image_practice_repast_1;
                    this.avatarId = R.mipmap.image_avatar_1;
                }}, new MainFragment.Model() {{
                    this.name = "香菇蒸鸟蛋";
                    this.nickname = "淑女算个鸟";
                    this.imageId = R.mipmap.image_practice_repast_2;
                    this.avatarId = R.mipmap.image_avatar_2;
                }}, new MainFragment.Model() {{
                    this.name = "花溪牛肉粉";
                    this.nickname = "性感妩媚";
                    this.imageId = R.mipmap.image_practice_repast_3;
                    this.avatarId = R.mipmap.image_avatar_3;
                }}, new MainFragment.Model() {{
                    this.name = "破酥包";
                    this.nickname = "一丝丝纯真";
                    this.imageId = R.mipmap.image_practice_repast_4;
                    this.avatarId = R.mipmap.image_avatar_4;
                }}, new MainFragment.Model() {{
                    this.name = "盐菜饭";
                    this.nickname = "等着你回来";
                    this.imageId = R.mipmap.image_practice_repast_5;
                    this.avatarId = R.mipmap.image_avatar_5;
                }}, new MainFragment.Model() {{
                    this.name = "米豆腐";
                    this.nickname = "宝宝树人";
                    this.imageId = R.mipmap.image_practice_repast_6;
                    this.avatarId = R.mipmap.image_avatar_6;
                }});
    }

}
