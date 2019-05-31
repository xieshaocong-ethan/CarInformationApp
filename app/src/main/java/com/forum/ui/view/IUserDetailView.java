package com.forum.ui.view;

import android.support.annotation.NonNull;


import com.forum.model.entity.Topic;
import com.forum.model.entity.User;

import java.util.List;

public interface IUserDetailView {

    void onGetUserOk(@NonNull User user);

    void onGetCollectTopicListOk(@NonNull List<Topic> topicList);

    void onGetUserError(@NonNull String message);

    void onGetUserStart();

    void onGetUserFinish();

}
