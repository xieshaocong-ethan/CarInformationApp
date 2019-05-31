package com.forum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.example.car.R;
import com.forum.model.api.ApiDefine;
import com.forum.model.entity.Reply;
import com.forum.model.entity.Topic;
import com.forum.model.entity.TopicWithReply;
import com.forum.model.storage.LoginShared;
import com.forum.presenter.contract.IReplyPresenter;
import com.forum.presenter.contract.ITopicHeaderPresenter;
import com.forum.presenter.contract.ITopicPresenter;
import com.forum.presenter.implement.ReplyPresenter;
import com.forum.presenter.implement.TopicHeaderPresenter;
import com.forum.presenter.implement.TopicPresenter;
import com.forum.ui.dialog.CreateReplyDialog;
import com.forum.ui.jsbridge.TopicJavascriptInterface;
import com.forum.ui.listener.DoubleClickBackToContentTopListener;
import com.forum.ui.listener.FloatingActionButtonBehaviorListener;
import com.forum.ui.listener.NavigationFinishClickListener;
import com.forum.ui.util.Navigator;
import com.forum.ui.util.ThemeUtils;
import com.forum.ui.view.IBackToContentTopView;
import com.forum.ui.view.ICreateReplyView;
import com.forum.ui.view.IReplyView;
import com.forum.ui.view.ITopicHeaderView;
import com.forum.ui.view.ITopicView;
import com.forum.ui.widget.TopicWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicCompatActivity extends StatusBarActivity implements ITopicView, ITopicHeaderView, IReplyView, IBackToContentTopView, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.web_topic)
    TopicWebView webTopic;

    @BindView(R.id.fab_reply)
    FloatingActionButton fabReply;

    private String topicId;
    private Topic topic;

    private ICreateReplyView createReplyView;

    private ITopicPresenter topicPresenter;
    private ITopicHeaderPresenter topicHeaderPresenter;
    private IReplyPresenter replyPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeUtils.configThemeBeforeOnCreate(this, R.style.AppThemeLight, R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_compat);
        ButterKnife.bind(this);

        topicId = getIntent().getStringExtra(Navigator.TopicWithAutoCompat.EXTRA_TOPIC_ID);

        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));
        toolbar.inflateMenu(R.menu.topic);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(this));

        topicPresenter = new TopicPresenter(this, this);
        topicHeaderPresenter = new TopicHeaderPresenter(this, this);
        replyPresenter = new ReplyPresenter(this, this);

        createReplyView = CreateReplyDialog.createWithAutoTheme(this, topicId, this);

        webTopic.addOnScrollListener(new FloatingActionButtonBehaviorListener.ForWebView(fabReply));
        webTopic.setBridgeAndLoadPage(new TopicJavascriptInterface(this, createReplyView, topicHeaderPresenter, replyPresenter));

        refreshLayout.setColorSchemeResources(R.color.color_accent);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if (topic != null) {
                    Navigator.openShare(this, "《" + topic.getTitle() + "》\n" + ApiDefine.TOPIC_LINK_URL_PREFIX + topicId + "\n—— 来自CNode社区");
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRefresh() {
        topicPresenter.getTopicAsyncTask(topicId);
    }

    @OnClick(R.id.fab_reply)
    void onBtnReplyClick() {
        if (topic != null && LoginActivity.checkLogin(this)) {
            createReplyView.showWindow();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.REQUEST_DEFAULT && resultCode == RESULT_OK) {
            refreshLayout.setRefreshing(true);
            onRefresh();
        }
    }

    @Override
    public void onGetTopicOk(@NonNull TopicWithReply topic) {
        this.topic = topic;
        webTopic.updateTopicAndUserId(topic, LoginShared.getId(this));
    }

    @Override
    public void onGetTopicFinish() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void appendReplyAndUpdateViews(@NonNull Reply reply) {
        webTopic.appendReply(reply);
    }

    @Override
    public void onCollectTopicOk() {
        webTopic.updateTopicCollect(true);
    }

    @Override
    public void onDecollectTopicOk() {
        webTopic.updateTopicCollect(false);
    }

    @Override
    public void onUpReplyOk(@NonNull Reply reply) {
        webTopic.updateReply(reply);
    }

    @Override
    public void backToContentTop() {
        webTopic.scrollTo(0, 0);
    }

}
