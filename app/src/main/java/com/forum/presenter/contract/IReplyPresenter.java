package com.forum.presenter.contract;

import android.support.annotation.NonNull;

import com.forum.model.entity.Reply;


public interface IReplyPresenter {

    void upReplyAsyncTask(@NonNull Reply reply);

}
