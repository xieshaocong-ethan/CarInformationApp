package com.forum.ui.view;

import android.support.annotation.NonNull;

import com.forum.model.entity.LoginResult;


import retrofit2.Call;

public interface ILoginView {

    void onAccessTokenError(@NonNull String message);

    void onLoginOk(@NonNull String accessToken, @NonNull LoginResult loginResult);

    void onLoginStart(@NonNull Call<LoginResult> call);


    void onLoginFinish();

}
