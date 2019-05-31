package com.forum.ui.view;

import android.support.annotation.NonNull;

import com.forum.model.entity.Reply;


public interface ICreateReplyView {

    void showWindow();

    void dismissWindow();

    void onAt(@NonNull Reply target, @NonNull Integer targetPosition);

    void onContentError(@NonNull String message);

    void onReplyTopicOk(@NonNull Reply reply);


    void onReplyTopicStart();

    void onReplyTopicFinish();

}
