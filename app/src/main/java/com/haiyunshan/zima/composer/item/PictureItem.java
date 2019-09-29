package com.haiyunshan.zima.composer.item;

import android.graphics.BitmapFactory;
import android.net.Uri;

import com.haiyunshan.zima.composer.entity.PictureEntity;

import java.io.File;

public class PictureItem extends BaseItem<PictureEntity> {

    Uri mUri;

    public static final PictureItem create(File file) {
        PictureItem item = new PictureItem(file);
        return item;
    }

    PictureItem(File file) {
        this.mEntity = new PictureEntity();

        this.mUri = Uri.fromFile(file);
        int[] size = getImageWidthHeight(file);

        mEntity.setUri(mUri.toString());
        mEntity.setWidth(size[0]);
        mEntity.setHeight(size[1]);
    }

    PictureItem(PictureEntity entity) {
        super(entity);

        this.mUri = Uri.parse(entity.getUri());
    }

    @Override
    public PictureEntity getEntity() {
        return super.getEntity();
    }

    public CharSequence getName() {
        return mEntity.getName();
    }

    public void setName(CharSequence text) {
        mEntity.setName(text.toString());
    }

    public Uri getUri() {
        return mUri;
    }

    public int getWidth() {
        return mEntity.getWidth();
    }

    public int getHeight() {
        return mEntity.getHeight();
    }

    static int[] getImageWidthHeight(File file){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(file.getAbsolutePath(), options); // 此时返回的bitmap为null

        return new int[]{options.outWidth,options.outHeight};
    }
}
