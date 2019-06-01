package com.forum.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;


import com.example.car.R;
import com.forum.model.api.ApiClient;
import com.forum.model.api.ForegroundCallback;
import com.forum.model.entity.DataResult;
import com.forum.model.entity.ErrorResult;
import com.forum.model.entity.Topic;
import com.forum.model.entity.User;
import com.forum.presenter.contract.IUserDetailPresenter;
import com.forum.ui.util.ActivityUtils;
import com.forum.ui.view.IUserDetailView;
import com.forum.util.HandlerUtils;

import java.util.List;

import okhttp3.Headers;

public class UserDetailPresenter implements IUserDetailPresenter {

    private final Activity activity;
    private final IUserDetailView userDetailView;

    private boolean loading = false;

    public UserDetailPresenter(@NonNull Activity activity, @NonNull IUserDetailView userDetailView) {
        this.activity = activity;
        this.userDetailView = userDetailView;
    }

    @Override
    public void getUserAsyncTask(@NonNull String loginName) {
        if (!loading) {
            loading = true;
            userDetailView.onGetUserStart();
            ApiClient.service.getUser(loginName).enqueue(new ForegroundCallback<DataResult<User>>(activity) {

                private long startLoadingTime = System.currentTimeMillis();

                private long getPostTime() {
                    long postTime = 1000 - (System.currentTimeMillis() - startLoadingTime);
                    if (postTime > 0) {
                        return postTime;
                    } else {
                        return 0;
                    }
                }

                @Override
                public boolean onResultOk(int code, Headers headers, final DataResult<User> result) {
                    HandlerUtils.handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(activity)) {
                                userDetailView.onGetUserOk(result.getData());
                                onFinish();
                            }
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public boolean onResultError(final int code, Headers headers, final ErrorResult errorResult) {
                    HandlerUtils.handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(activity)) {
                                userDetailView.onGetUserError(code == 404 ? errorResult.getMessage() : activity.getString(R.string.data_load_faild_and_click_avatar_to_reload));
                                onFinish();
                            }
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public boolean onCallException(Throwable t, ErrorResult errorResult) {
                    HandlerUtils.handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (ActivityUtils.isAlive(activity)) {
                                userDetailView.onGetUserError(activity.getString(R.string.data_load_faild_and_click_avatar_to_reload));
                                onFinish();
                            }
                        }

                    }, getPostTime());
                    return true;
                }

                @Override
                public void onFinish() {
                    userDetailView.onGetUserFinish();
                    loading = false;
                }

            });
            ApiClient.service.getCollectTopicList(loginName).enqueue(new ForegroundCallback<DataResult<List<Topic>>>(activity) {

                @Override
                public boolean onResultOk(int code, Headers headers, DataResult<List<Topic>> result) {
                    userDetailView.onGetCollectTopicListOk(result.getData());
                    return false;
                }

            });
        }
    }

}
