package com.forum.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;


import com.forum.model.api.ApiClient;
import com.forum.model.api.SessionCallback;
import com.forum.model.entity.Result;
import com.forum.model.storage.LoginShared;
import com.forum.presenter.contract.ITopicHeaderPresenter;
import com.forum.ui.view.ITopicHeaderView;

import okhttp3.Headers;

public class TopicHeaderPresenter implements ITopicHeaderPresenter {

    private final Activity activity;
    private final ITopicHeaderView topicHeaderView;

    public TopicHeaderPresenter(@NonNull Activity activity, @NonNull ITopicHeaderView topicHeaderView) {
        this.activity = activity;
        this.topicHeaderView = topicHeaderView;
    }

    @Override
    public void collectTopicAsyncTask(@NonNull String topicId) {
        ApiClient.service.collectTopic(LoginShared.getAccessToken(activity), topicId).enqueue(new SessionCallback<Result>(activity) {

            @Override
            public boolean onResultOk(int code, Headers headers, Result result) {
                topicHeaderView.onCollectTopicOk();
                return false;
            }

        });
    }

    @Override
    public void decollectTopicAsyncTask(@NonNull String topicId) {
        ApiClient.service.decollectTopic(LoginShared.getAccessToken(activity), topicId).enqueue(new SessionCallback<Result>(activity) {

            @Override
            public boolean onResultOk(int code, Headers headers, Result result) {
                topicHeaderView.onDecollectTopicOk();
                return false;
            }

        });
    }

}
