package com.forum.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.example.car.R;
import com.forum.model.api.ApiClient;
import com.forum.model.api.SessionCallback;
import com.forum.model.entity.Author;
import com.forum.model.entity.Reply;
import com.forum.model.entity.ReplyTopicResult;
import com.forum.model.storage.LoginShared;
import com.forum.model.storage.SettingShared;
import com.forum.presenter.contract.ICreateReplyPresenter;
import com.forum.ui.view.ICreateReplyView;

import org.joda.time.DateTime;

import java.util.ArrayList;

import okhttp3.Headers;

public class CreateReplyPresenter implements ICreateReplyPresenter {

    private final Activity activity;
    private final ICreateReplyView createReplyView;

    public CreateReplyPresenter(@NonNull Activity activity, @NonNull ICreateReplyView createReplyView) {
        this.activity = activity;
        this.createReplyView = createReplyView;
    }

    @Override
    public void createReplyAsyncTask(@NonNull String topicId, String content, final String targetId) {
        if (TextUtils.isEmpty(content)) {
            createReplyView.onContentError(activity.getString(R.string.content_empty_error_tip));
        } else {
            final String finalContent;
            if (SettingShared.isEnableTopicSign(activity)) { // 添加小尾巴
                finalContent = content + "\n\n" + SettingShared.getTopicSignContent(activity);
            } else {
                finalContent = content;
            }
            createReplyView.onReplyTopicStart();
            ApiClient.service.createReply(topicId, LoginShared.getAccessToken(activity), finalContent, targetId).enqueue(new SessionCallback<ReplyTopicResult>(activity) {

                @Override
                public boolean onResultOk(int code, Headers headers, ReplyTopicResult result) {
                    Reply reply = new Reply();
                    reply.setId(result.getReplyId());
                    Author author = new Author();
                    author.setLoginName(LoginShared.getLoginName(activity));
                    author.setAvatarUrl(LoginShared.getAvatarUrl(activity));
                    reply.setAuthor(author);
                    reply.setContentFromLocal(finalContent); // 这里要使用本地的访问器
                    reply.setCreateAt(new DateTime());
                    reply.setUpList(new ArrayList<String>());
                    reply.setReplyId(targetId);
                    createReplyView.onReplyTopicOk(reply);
                    return false;
                }

                @Override
                public void onFinish() {
                    createReplyView.onReplyTopicFinish();
                }

            });
        }
    }

}
