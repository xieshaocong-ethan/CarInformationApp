package com.forum.model.api;

import com.forum.model.entity.CreateTopicResult;
import com.forum.model.entity.DataResult;
import com.forum.model.entity.LoginResult;
import com.forum.model.entity.Notification;
import com.forum.model.entity.ReplyTopicResult;
import com.forum.model.entity.Result;
import com.forum.model.entity.Tab;
import com.forum.model.entity.Topic;
import com.forum.model.entity.TopicWithReply;
import com.forum.model.entity.UpReplyResult;
import com.forum.model.entity.User;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //=====
    // 话题
    //=====

    @GET("topics")
    Call<DataResult<List<Topic>>> getTopicList(
            @Query("tab") Tab tab,
            @Query("page") Integer page,
            @Query("limit") Integer limit,
            @Query("mdrender") Boolean mdrender
    );

    @GET("topic/{topicId}")
    Call<DataResult<TopicWithReply>> getTopic(
            @Path("topicId") String topicId,
            @Query("accesstoken") String accessToken,
            @Query("mdrender") Boolean mdrender
    );

    @POST("topics")
    @FormUrlEncoded
    Call<CreateTopicResult> createTopic(
            @Field("accesstoken") String accessToken,
            @Field("tab") Tab tab,
            @Field("title") String title,
            @Field("content") String content
    );

    //=========
    // 话题收藏
    //=========

    @POST("topic_collect/collect")
    @FormUrlEncoded
    Call<Result> collectTopic(
            @Field("accesstoken") String accessToken,
            @Field("topic_id") String topicId
    );

    @POST("topic_collect/de_collect")
    @FormUrlEncoded
    Call<Result> decollectTopic(
            @Field("accesstoken") String accessToken,
            @Field("topic_id") String topicId
    );

    @GET("topic_collect/{loginName}")
    Call<DataResult<List<Topic>>> getCollectTopicList(
            @Path("loginName") String loginName
    );

    //=====
    // 回复
    //=====

    @POST("topic/{topicId}/replies")
    @FormUrlEncoded
    Call<ReplyTopicResult> createReply(
            @Path("topicId") String topicId,
            @Field("accesstoken") String accessToken,
            @Field("content") String content,
            @Field("reply_id") String targetId
    );

    @POST("reply/{replyId}/ups")
    @FormUrlEncoded
    Call<UpReplyResult> upReply(
            @Path("replyId") String replyId,
            @Field("accesstoken") String accessToken
    );

    //=====
    // 用户
    //=====

    @GET("user/{loginName}")
    Call<DataResult<User>> getUser(
            @Path("loginName") String loginName
    );

    @POST("accesstoken")
    @FormUrlEncoded
    Call<LoginResult> accessToken(
            @Field("accesstoken") String accessToken
    );

    //=========
    // 消息通知
    //=========

    @GET("message/count")
    Call<DataResult<Integer>> getMessageCount(
            @Query("accesstoken") String accessToken
    );

    @GET("messages")
    Call<DataResult<Notification>> getMessages(
            @Query("accesstoken") String accessToken,
            @Query("mdrender") Boolean mdrender
    );

    @POST("message/mark_all")
    @FormUrlEncoded
    Call<Result> markAllMessageRead(
            @Field("accesstoken") String accessToken
    );

}
