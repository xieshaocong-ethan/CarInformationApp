package com.forum.model.api;

import com.forum.model.entity.ErrorResult;
import com.forum.model.entity.Result;


import okhttp3.Headers;

public interface CallbackLifecycle<T extends Result> {

    boolean onResultOk(int code, Headers headers, T result);

    boolean onResultError(int code, Headers headers, ErrorResult errorResult);

    boolean onCallCancel();

    boolean onCallException(Throwable t, ErrorResult errorResult);


    void onFinish();

}
