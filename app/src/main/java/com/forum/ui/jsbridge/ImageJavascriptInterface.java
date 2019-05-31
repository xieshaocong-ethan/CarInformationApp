package com.forum.ui.jsbridge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import com.forum.ui.activity.ImagePreviewActivity;


public final class ImageJavascriptInterface {

    public static final String NAME = "imageBridge";

    private final Context context;

    public ImageJavascriptInterface(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @JavascriptInterface
    public void openImage(String imageUrl) {
        ImagePreviewActivity.start(context, imageUrl);
    }

}
