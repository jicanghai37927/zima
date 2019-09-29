package com.haiyunshan.zima.composer.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haiyunshan.zima.R;
import com.haiyunshan.zima.composer.ComposerFragment;

public class SeparateHolder extends BaseHolder {

    public static final SeparateHolder create(ComposerFragment parent, ViewGroup container) {
        LayoutInflater inflater = parent.getLayoutInflater();
        int resource = R.layout.layout_composer_separate_item;
        View view = inflater.inflate(resource, container, false);

        SeparateHolder holder = new SeparateHolder(parent, view);
        return holder;
    }

    public SeparateHolder(ComposerFragment parent, View itemView) {
        super(parent, itemView);
    }

}
