package com.jpresti.randomusers.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jpresti.randomusers.data.external.RandomUserRequester;

public class NetworkRequester {

    protected static NetworkRequester instance;
    protected static final int IMAGE_CACHE_SIZE = RandomUserRequester.QUANTITY_RESULTS;

    protected RequestQueue requestQueue;
    protected ImageLoader imageLoader;
    protected Context applicationContext;

    protected NetworkRequester(Context context) {
        applicationContext = context.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized NetworkRequester getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkRequester(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(applicationContext);
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(getRequestQueue(),
                    new ImageLoader.ImageCache() {
                        private final LruCache<String, Bitmap> cache = new LruCache<>(IMAGE_CACHE_SIZE);

                        @Override
                        public Bitmap getBitmap(String url) {
                            return cache.get(url);
                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                            cache.put(url, bitmap);
                        }
                    }
            );
        }
        return imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelRequests(String tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

}
