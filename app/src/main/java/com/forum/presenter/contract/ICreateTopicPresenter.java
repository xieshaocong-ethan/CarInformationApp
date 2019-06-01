package com.forum.presenter.contract;

import android.support.annotation.NonNull;

import com.forum.model.entity.Tab;

public interface ICreateTopicPresenter {

    void createTopicAsyncTask(@NonNull Tab tab, String title, String content);

}
