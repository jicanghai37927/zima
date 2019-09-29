package com.haiyunshan.zima.composer.entity;

import com.google.gson.annotations.SerializedName;
import com.haiyunshan.zima.composer.entity.style.SpanEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ParagraphEntity extends BaseEntity {

    @SerializedName("text")
    String mText;

    @SerializedName("spans")
    ArrayList<SpanEntity> mSpans;

    public ParagraphEntity() {
        super("paragraph");

        this.mText = "";
        this.mSpans = new ArrayList<>();
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public List<SpanEntity> getSpans() {
        return mSpans;
    }
}
