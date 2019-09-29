package com.haiyunshan.zima.color;

import android.graphics.Color;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.haiyunshan.zima.ZimaApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class ColorPanel {

    private static ColorPanel sInstance;

    @SerializedName("color")
    String[][] mColors;

    public static final ColorPanel instance() {
        if (sInstance == null) {
            sInstance = create();
        }

        return sInstance;
    }

    public String[][] getColors() {
        return mColors;
    }

    static final ColorPanel create() {
        ColorPanel instance = null;

        try {
            InputStream is = ZimaApp.instance().getAssets().open("color_panel.json");
            InputStreamReader isr = new InputStreamReader(is, "utf-8");

            Gson gson = new Gson();
            instance = gson.fromJson(isr, ColorPanel.class);

            isr.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return instance;
    }
}
