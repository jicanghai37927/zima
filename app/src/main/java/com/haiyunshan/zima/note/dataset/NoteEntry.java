package com.haiyunshan.zima.note.dataset;

import com.google.gson.annotations.SerializedName;
import com.haiyunshan.zima.dataset.BaseEntry;

public class NoteEntry extends BaseEntry {

    @SerializedName("title")
    protected String mTitle;

    @SerializedName("subtitle")
    protected String mSubtitle;

    @SerializedName("created")
    protected long mCreated;

    public NoteEntry(String id) {
        super(id);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public long getCreated() {
        return mCreated;
    }

    public void setCreated(long mCreated) {
        this.mCreated = mCreated;
    }
}
