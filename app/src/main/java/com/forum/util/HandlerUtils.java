package com.forum.util;

import android.os.Handler;
import android.os.Looper;

public final class HandlerUtils {

    private HandlerUtils() {}

    public static final Handler handler = new Handler(Looper.getMainLooper());

}
