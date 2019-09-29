package com.haiyunshan.zima.dataset;

import com.google.gson.annotations.SerializedName;

public class BaseEntry {

    @SerializedName("id")
    protected String mId;

    public BaseEntry(String id) {
        this.mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

}
