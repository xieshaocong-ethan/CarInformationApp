package com.forum.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;


import com.forum.model.api.ApiClient;
import com.forum.model.api.ApiDefine;
import com.forum.model.api.SessionCallback;
import com.forum.model.entity.DataResult;
import com.forum.model.entity.TopicWithReply;
import com.forum.model.storage.LoginShared;
import com.forum.presenter.contract.ITopicPresenter;
import com.forum.ui.view.ITopicView;

import okhttp3.Headers;

public class TopicPresenter implements ITopicPresenter {

    private final Activity activity;
    private final ITopicView topicView;

    public TopicPresenter(@NonNull Activity activity, @NonNull ITopicView topicView) {
        this.activity = activity;
        this.topicView = topicView;
    }

    @Override
    public void getTopicAsyncTask(@NonNull String topicId) {
        ApiClient.service.getTopic(topicId, LoginShared.getAccessToken(activity), ApiDefine.MD_RENDER).enqueue(new SessionCallback<DataResult<TopicWithReply>>(activity) {

            @Override
            public boolean onResultOk(int code, Headers headers, DataResult<TopicWithReply> result) {
                topicView.onGetTopicOk(result.getData());
                return false;
            }

            @Override
            public void onFinish() {
                topicView.onGetTopicFinish();
            }

        });
    }

}
