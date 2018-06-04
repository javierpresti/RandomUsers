package com.jpresti.randomusers.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jpresti.randomusers.data.RandomUserRequester;

public class NetworkRequester {

    protected static NetworkRequester instance;

    protected RequestQueue requestQueue;
    protected ImageLoader imageLoader;

    protected NetworkRequester(Context context) {
        requestQueue = getRequestQueue(context);
    }

    public static synchronized NetworkRequester getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkRequester(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader(Context context) {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(getRequestQueue(context),
                    new ImageLoader.ImageCache() {
                        private final LruCache<String, Bitmap> cache =
                                new LruCache<>(RandomUserRequester.QUANTITY_RESULTS);

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

    public <T> void addToRequestQueue(Context context, Request<T> req) {
        getRequestQueue(context).add(req);
    }

    public void cancelRequests(String tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

}
