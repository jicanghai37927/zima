package com.haiyunshan.zima.utils;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.haiyunshan.zima.ZimaApp;

public class Utils {

    public static final float px2dp(int pixels) {
        ZimaApp context = ZimaApp.instance();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        float dp = pixels / metrics.density;
        return dp;
    }

    public static final float dp2px(float dp) {
        ZimaApp context = ZimaApp.instance();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
        return px;
    }

    public static final String fromColor(int color) {
        String str = Integer.toHexString(color);
        if (str.length() < 6) { // 补足6位
            int count = (6 - str.length());
            for (int i = 0; i < count; i++) {
                str = "0" + str;
            }
        } else if (str.length() == 7) { // 补足8位
            str = "0" + str;
        }

        return "#" + str;
    }

    public static final int parseColor(String colorString) {
        return Color.parseColor(colorString);
    }
}
