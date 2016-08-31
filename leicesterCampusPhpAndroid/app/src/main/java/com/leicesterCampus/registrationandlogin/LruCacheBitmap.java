package com.leicesterCampus.registrationandlogin;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class LruCacheBitmap extends LruCache implements ImageLoader.ImageCache{

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    public LruCacheBitmap(){
        this(getDefaultLruCacheSize());
    }

    public LruCacheBitmap(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return (Bitmap) get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url,bitmap);
    }
}
