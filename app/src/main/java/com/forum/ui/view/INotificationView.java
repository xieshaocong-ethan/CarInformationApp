package com.forum.ui.view;

import android.support.annotation.NonNull;

import org.cnodejs.android.md.model.entity.Notification;

public interface INotificationView {

    void onGetMessagesOk(@NonNull Notification notification);

    void onGetMessagesFinish();

    void onMarkAllMessageReadOk();

}
