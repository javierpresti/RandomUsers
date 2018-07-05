package com.jpresti.randomusers.util;

import android.util.Log;

import com.jpresti.randomusers.BuildConfig;

/**
 * Following guidelines, VERBOSE and DEBUG logs <b>are</b> disabled on release builds
 */
public class LogUtils {

    public static void v(Class clazz, String msg) {
        if (BuildConfig.DEBUG) Log.v(clazz.getSimpleName(), msg);
    }

    public static void d(Class clazz, String msg) {
        if (BuildConfig.DEBUG) Log.d(clazz.getSimpleName(), msg);
    }

    public static void i(Class clazz, String msg) {
        Log.i(clazz.getSimpleName(), msg);
    }

    public static void w(Class clazz, String msg) {
        Log.w(clazz.getSimpleName(), msg);
    }

    public static void e(Class clazz, String msg) {
        Log.e(clazz.getSimpleName(), msg);
    }
}
