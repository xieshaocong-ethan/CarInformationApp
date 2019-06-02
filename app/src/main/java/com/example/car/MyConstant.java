package com.example.car;

import android.os.Environment;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2018/6/6.
 */

public class MyConstant {

    public static final String PIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CAR";
    public static final String url = "http://192.168.0.109:8080/";

    public static void setCarImg(String imgPath, int cacheSize, ImageView imageView) {

        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueues(),
                new BitmapLruCache(cacheSize) {});
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.image_placeholder, R.drawable.image_error);//加载时图片，默认图片
        imageLoader.get(MyConstant.url+imgPath, listener);
    }
}
