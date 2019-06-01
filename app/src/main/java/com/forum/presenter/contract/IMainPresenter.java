package com.forum.presenter.contract;

import android.support.annotation.NonNull;

import com.forum.model.entity.Tab;

public interface IMainPresenter {

    void switchTab(@NonNull Tab tab);

    void refreshTopicListAsyncTask();

    void loadMoreTopicListAsyncTask(int page);

    void getUserAsyncTask();

    void getMessageCountAsyncTask();

}
