package com.haiyunshan.zima.composer.item;

import com.haiyunshan.zima.composer.entity.BaseEntity;

public class BaseItem<T extends BaseEntity> {

    T mEntity;

    BaseItem() {

    }

    BaseItem(T entity) {
        this.mEntity = entity;
    }

    public T getEntity() {
        return mEntity;
    }

    public boolean isEmpty() {
        return false;
    }

}
