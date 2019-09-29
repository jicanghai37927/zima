package com.haiyunshan.zima.font.dataset;

import com.google.gson.annotations.SerializedName;
import com.haiyunshan.zima.dataset.BaseEntry;

public class FontEntry extends BaseEntry {

    @SerializedName("name")
    String mName;

    @SerializedName("uri")
    String mUri;

    public FontEntry(String id, String name, String uri) {
        super(id);

        this.mName = name;
        this.mUri = uri;
    }

    public String getName() {
        return mName;
    }

    public String getUri() {
        return mUri;
    }
}
