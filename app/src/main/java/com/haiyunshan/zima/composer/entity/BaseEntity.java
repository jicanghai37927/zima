package com.haiyunshan.zima.composer.entity;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class BaseEntity {

    @SerializedName("type")
    protected String mType;

    protected BaseEntity(String type) {
        this.mType = type;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }
}
