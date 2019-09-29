package com.haiyunshan.zima.dataset;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BaseDataset<T extends BaseEntry> {

    @SerializedName("list")
    ArrayList<T> mList;

    public BaseDataset() {
        this.mList = new ArrayList<>();
    }

    public List<T> getList() {
        return mList;
    }

    public void add(T e) {
        mList.add(e);
    }

    public int size() {
        return mList.size();
    }

    public T obtain(String id) {
        for (T item : mList) {
            if (item.mId.equalsIgnoreCase(id)) {
                return item;
            }
        }

        return null;
    }
}
