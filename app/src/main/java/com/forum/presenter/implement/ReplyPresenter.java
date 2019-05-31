package com.forum.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.forum.model.api.ApiClient;
import com.forum.model.api.SessionCallback;
import com.forum.model.entity.Reply;
import com.forum.model.entity.UpReplyResult;
import com.forum.model.storage.LoginShared;
import com.forum.presenter.contract.IReplyPresenter;
import com.forum.ui.view.IReplyView;


import okhttp3.Headers;

public class ReplyPresenter implements IReplyPresenter {

    private final Activity activity;
    private final IReplyView replyView;

    public ReplyPresenter(@NonNull Activity activity, @NonNull IReplyView replyView) {
        this.activity = activity;
        this.replyView = replyView;
    }

    @Override
    public void upReplyAsyncTask(@NonNull final Reply reply) {
        ApiClient.service.upReply(reply.getId(), LoginShared.getAccessToken(activity)).enqueue(new SessionCallback<UpReplyResult>(activity) {

            @Override
            public boolean onResultOk(int code, Headers headers, UpReplyResult result) {
                if (result.getAction() == Reply.UpAction.up) {
                    reply.getUpList().add(LoginShared.getId(activity));
                } else if (result.getAction() == Reply.UpAction.down) {
                    reply.getUpList().remove(LoginShared.getId(activity));
                }
                replyView.onUpReplyOk(reply);
                return false;
            }

        });
    }

}
