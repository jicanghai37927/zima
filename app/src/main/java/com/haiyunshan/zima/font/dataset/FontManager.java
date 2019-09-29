package com.haiyunshan.zima.font.dataset;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.haiyunshan.zima.R;
import com.haiyunshan.zima.ZimaApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FontManager {

    private static FontManager sInstance;

    HashMap<FontEntry, WeakReference<Typeface>> mFonts;

    ArrayList<FontEntry> mList;

    public static final FontManager instance() {
        if (sInstance == null) {
            sInstance = new FontManager();
        }

        return sInstance;
    }

    private FontManager() {
        this.mList = new ArrayList<>(7);
        this.mFonts = new HashMap<>(7);

        // 系统字体
        {
            FontDataset ds = getSystem();
            mList.addAll(ds.getList());
        }

        // asset字体
        {
            FontDataset ds = getAsset();
            if (ds != null) {
                mList.addAll(ds.getList());
            }
        }
    }

    public List<FontEntry> getList() {
        return mList;
    }

    public FontEntry obtain(String id) {
        for (FontEntry e : mList) {
            if (e.getId().equalsIgnoreCase(id)) {
                return e;
            }
        }

        return null;
    }

    public Typeface getTypeface(String id) {
        FontEntry e = obtain(id);
        if (e == null) {
            return Typeface.DEFAULT;
        }

        return getTypeface(e);
    }

    public Typeface getTypeface(FontEntry entry) {
        Typeface font = Typeface.DEFAULT;

        String uri = entry.getUri();
        if (TextUtils.isEmpty(uri)) {

        } else if (uri.equalsIgnoreCase("sans-serif")) {
            font = Typeface.SANS_SERIF;
        } else if (uri.equalsIgnoreCase("serif")) {
            font = Typeface.SERIF;
        } else if (uri.equalsIgnoreCase("monospace")) {
            font = Typeface.MONOSPACE;
        } else {
            font = this.obtainTypeface(entry);
        }

        return font;
    }

    Typeface obtainTypeface(FontEntry key) {
        Typeface tf = null;

        WeakReference<Typeface> ref = mFonts.get(key);
        if (ref != null) {
            tf = ref.get();
        }

        if (tf != null) {
            return tf;
        }

        // 从assets创建
        {
            AssetManager assets = ZimaApp.instance().getAssets();
            String path = "font/files/" + key.getUri();
            tf = Typeface.createFromAsset(assets, path);
        }

        mFonts.remove(key);
        ref = new WeakReference<>(tf);
        mFonts.put(key, ref);

        return tf;
    }

    FontDataset getSystem() {
        FontDataset ds = new FontDataset();

        Context context = ZimaApp.instance();
        String name = context.getString(R.string.default_system_font_name);

        String[][] array = new String[][] {
                {"d6f69143f70249319a14d98bfccad0fa", name},
                {"e1e8246d0af543a8ba481044c37a1576", "sans-serif"},
                {"fcc1f0c998174888b9044ae9c926cd1a", "serif"},
                {"a79cd7d386bc4928924cc66af11bfa2f", "monospace"}
        };

        for (String[] a : array) {
            FontEntry e = new FontEntry(a[0], a[1], "");
            ds.add(e);
        }

        return ds;
    }

    FontDataset getAsset() {
        FontDataset ds = null;

        try {
            Context context = ZimaApp.instance();
            String path = "font/font_ds.json";

            InputStream is = context.getAssets().open(path);
            InputStreamReader isr = new InputStreamReader(is, "utf-8");

            Gson gson = new Gson();
            ds = gson.fromJson(isr, FontDataset.class);

            isr.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return ds;
    }
}
