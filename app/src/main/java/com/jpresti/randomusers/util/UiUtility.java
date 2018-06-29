package com.jpresti.randomusers.util;

import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.util.DisplayMetrics;

public class UiUtility {

    public static int calculateNoOfColumns(Resources resources, @DimenRes int id) {
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return displayMetrics.widthPixels / resources.getDimensionPixelSize(id);
    }

}
