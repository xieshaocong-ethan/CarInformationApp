package com.forum.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.forum.model.api.ApiClient;
import com.forum.model.api.ApiDefine;
import com.forum.model.api.SessionCallback;
import com.forum.model.entity.DataResult;
import com.forum.model.entity.Notification;
import com.forum.model.entity.Result;
import com.forum.model.storage.LoginShared;
import com.forum.presenter.contract.INotificationPresenter;
import com.forum.ui.view.INotificationView;


import okhttp3.Headers;

public class NotificationPresenter implements INotificationPresenter {

    private final Activity activity;
    private final INotificationView notificationView;

    public NotificationPresenter(@NonNull Activity activity, @NonNull INotificationView notificationView) {
        this.activity = activity;
        this.notificationView = notificationView;
    }

    @Override
    public void getMessagesAsyncTask() {
        ApiClient.service.getMessages(LoginShared.getAccessToken(activity), ApiDefine.MD_RENDER).enqueue(new SessionCallback<DataResult<Notification>>(activity) {

            @Override
            public boolean onResultOk(int code, Headers headers, DataResult<Notification> result) {
                notificationView.onGetMessagesOk(result.getData());
                return false;
            }

            @Override
            public void onFinish() {
                notificationView.onGetMessagesFinish();
            }

        });
    }

    @Override
    public void markAllMessageReadAsyncTask() {
        ApiClient.service.markAllMessageRead(LoginShared.getAccessToken(activity)).enqueue(new SessionCallback<Result>(activity) {

            @Override
            public boolean onResultOk(int code, Headers headers, Result result) {
                notificationView.onMarkAllMessageReadOk();
                return false;
            }

        });
    }

}
