package com.forum.model.api;

import android.support.annotation.NonNull;


import com.example.car.BuildConfig;
import com.forum.model.util.EntityUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpHackUtils;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private ApiClient() {}

    public static final ApiService service = new Retrofit.Builder()
            .baseUrl(ApiDefine.API_BASE_URL)
            .client(new OkHttpClient.Builder()
                    .addInterceptor(createUserAgentInterceptor())
                    .addInterceptor(createHttpLoggingInterceptor())
                    .build())
            .addConverterFactory(GsonConverterFactory.create(EntityUtils.gson))
            .build()
            .create(ApiService.class);

    @NonNull
    private static Interceptor createUserAgentInterceptor() {
        return new Interceptor() {

            private static final String HEADER_USER_AGENT = "User-Agent";

            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                OkHttpHackUtils.setRequestHeaderLenient(builder, HEADER_USER_AGENT, ApiDefine.USER_AGENT);
                return chain.proceed(builder.build());
            }

        };
    }

    @NonNull
    private static Interceptor createHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return loggingInterceptor;
    }

}
