package com.jpresti.randomusers.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jpresti.randomusers.model.external.RandomUserRequester;

public class NetworkRequester {

    protected static NetworkRequester sInstance;
    protected static final int IMAGE_CACHE_SIZE = RandomUserRequester.QUANTITY_RESULTS;

    protected RequestQueue mRequestQueue;
    protected ImageLoader mImageLoader;
    protected Context mApplicationContext;

    protected NetworkRequester(Context context) {
        mApplicationContext = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkRequester getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NetworkRequester(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mApplicationContext);
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(getRequestQueue(),
                    new ImageLoader.ImageCache() {
                        private final LruCache<String, Bitmap> mCache =
                                new LruCache<>(IMAGE_CACHE_SIZE);

                        @Override
                        public Bitmap getBitmap(String url) {
                            return mCache.get(url);
                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                            mCache.put(url, bitmap);
                        }
                    }
            );
        }
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelRequests(String tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
