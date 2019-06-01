package com.forum.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.car.R;
import com.forum.ui.util.Navigator;
import com.forum.ui.util.ToastUtils;


public class StandardLinkRouterActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Navigator.openStandardLink(this, getIntent().getDataString())) {
            ToastUtils.with(this).show(R.string.invalid_link);
        }
        finish();
    }

}
