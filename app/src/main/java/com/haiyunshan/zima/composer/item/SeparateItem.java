package com.haiyunshan.zima.composer.item;

import com.haiyunshan.zima.composer.entity.SeparateEntity;

public class SeparateItem extends BaseItem<SeparateEntity> {

    public static final SeparateItem create() {
        SeparateItem item = new SeparateItem();
        return item;
    }

    SeparateItem() {
        this.mEntity = new SeparateEntity();
    }

    SeparateItem(SeparateEntity entity) {
        super(entity);
    }
}
