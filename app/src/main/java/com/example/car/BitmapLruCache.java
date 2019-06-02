package com.example.car;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {
    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }

    protected int sizeOf(String key, Bitmap bitmap) {
        return (bitmap.getRowBytes() * bitmap.getHeight());
    }

    public Bitmap getBitmap(String url) {
        return ((Bitmap) get(url));
    }

    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}