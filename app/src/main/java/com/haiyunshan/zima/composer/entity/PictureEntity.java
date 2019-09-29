package com.haiyunshan.zima.composer.entity;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class PictureEntity extends BaseEntity {

    @SerializedName("name")
    String mName;

    @SerializedName("uri")
    String mUri;

    @SerializedName("width")
    int mWidth;

    @SerializedName("height")
    int mHeight;

    public PictureEntity() {
        super("picture");
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String mUri) {
        this.mUri = mUri;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }
}
