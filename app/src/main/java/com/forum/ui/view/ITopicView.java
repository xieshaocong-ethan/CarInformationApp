package com.forum.ui.view;

import android.support.annotation.NonNull;

import com.forum.model.entity.Reply;
import com.forum.model.entity.TopicWithReply;


public interface ITopicView {

    void onGetTopicOk(@NonNull TopicWithReply topic);


    void onGetTopicFinish();

    void appendReplyAndUpdateViews(@NonNull Reply reply);

}
