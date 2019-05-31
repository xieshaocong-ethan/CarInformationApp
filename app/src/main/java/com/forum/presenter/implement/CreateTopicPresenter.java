package com.forum.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.example.car.R;
import com.forum.model.api.ApiClient;
import com.forum.model.api.SessionCallback;
import com.forum.model.entity.CreateTopicResult;
import com.forum.model.entity.Tab;
import com.forum.model.storage.LoginShared;
import com.forum.model.storage.SettingShared;
import com.forum.presenter.contract.ICreateTopicPresenter;
import com.forum.ui.view.ICreateTopicView;

import okhttp3.Headers;

public class CreateTopicPresenter implements ICreateTopicPresenter {

    private final Activity activity;
    private final ICreateTopicView createTopicView;

    public CreateTopicPresenter(@NonNull Activity activity, @NonNull ICreateTopicView createTopicView) {
        this.activity = activity;
        this.createTopicView = createTopicView;
    }

    @Override
    public void createTopicAsyncTask(@NonNull Tab tab, String title, String content) {
        if (TextUtils.isEmpty(title) || title.length() < 10) {
            createTopicView.onTitleError(activity.getString(R.string.title_empty_error_tip));
        } else if (TextUtils.isEmpty(content)) {
            createTopicView.onContentError(activity.getString(R.string.content_empty_error_tip));
        } else {
            if (SettingShared.isEnableTopicSign(activity)) { // 添加小尾巴
                content += "\n\n" + SettingShared.getTopicSignContent(activity);
            }
            createTopicView.onCreateTopicStart();
            ApiClient.service.createTopic(LoginShared.getAccessToken(activity), tab, title, content).enqueue(new SessionCallback<CreateTopicResult>(activity) {

                @Override
                public boolean onResultOk(int code, Headers headers, CreateTopicResult result) {
                    createTopicView.onCreateTopicOk(result.getTopicId());
                    return false;
                }

                @Override
                public void onFinish() {
                    createTopicView.onCreateTopicFinish();
                }

            });
        }
    }

}
