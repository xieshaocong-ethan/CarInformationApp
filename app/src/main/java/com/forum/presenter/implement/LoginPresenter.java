package com.forum.presenter.implement;

import android.app.Activity;
import android.support.annotation.NonNull;


import com.example.car.R;
import com.forum.model.api.ApiClient;
import com.forum.model.api.SessionCallback;
import com.forum.model.entity.ErrorResult;
import com.forum.model.entity.LoginResult;
import com.forum.presenter.contract.ILoginPresenter;
import com.forum.ui.view.ILoginView;
import com.forum.util.FormatUtils;

import okhttp3.Headers;
import retrofit2.Call;

public class LoginPresenter implements ILoginPresenter {

    private final Activity activity;
    private final ILoginView loginView;

    public LoginPresenter(@NonNull Activity activity, @NonNull ILoginView loginView) {
        this.activity = activity;
        this.loginView = loginView;
    }

    @Override
    public void loginAsyncTask(final String accessToken) {
        if (!FormatUtils.isAccessToken(accessToken)) {
            loginView.onAccessTokenError(activity.getString(R.string.access_token_format_error));
        } else {
            Call<LoginResult> call = ApiClient.service.accessToken(accessToken);
            loginView.onLoginStart(call);
            call.enqueue(new SessionCallback<LoginResult>(activity) {

                @Override
                public boolean onResultOk(int code, Headers headers, LoginResult loginResult) {
                    loginView.onLoginOk(accessToken, loginResult);
                    return false;
                }

                @Override
                public boolean onResultAuthError(int code, Headers headers, ErrorResult errorResult) {
                    loginView.onAccessTokenError(activity.getString(R.string.access_token_auth_error));
                    return false;
                }

                @Override
                public void onFinish() {
                    loginView.onLoginFinish();
                }

            });
        }
    }

}
