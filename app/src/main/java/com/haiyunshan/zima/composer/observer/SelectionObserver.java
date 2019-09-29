package com.haiyunshan.zima.composer.observer;

import com.haiyunshan.zima.composer.holder.ParagraphHolder;

public interface SelectionObserver {

    void onSelectionChanged(ParagraphHolder holder, int start, int end);

}
