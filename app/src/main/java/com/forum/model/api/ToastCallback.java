package com.forum.model.api;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.forum.model.entity.ErrorResult;
import com.forum.model.entity.Result;
import com.forum.ui.util.ActivityUtils;
import com.forum.ui.util.ToastUtils;


import okhttp3.Headers;

public class ToastCallback<T extends Result> extends ForegroundCallback<T> {

    public ToastCallback(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    public boolean onResultError(int code, Headers headers, ErrorResult errorResult) {
        return onAnyError(errorResult);
    }

    @Override
    public boolean onCallException(Throwable t, ErrorResult errorResult) {
        return onAnyError(errorResult);
    }

    public boolean onAnyError(ErrorResult errorResult) {
        toastError(errorResult);
        return false;
    }

    protected final void toastError(ErrorResult errorResult) {
        Activity activity = getActivity();
        if (ActivityUtils.isAlive(activity)) {
            ToastUtils.with(activity).show(errorResult.getMessage());
        }
    }

}
