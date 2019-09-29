package com.haiyunshan.zima.composer.observable;

import android.database.Observable;

import com.haiyunshan.zima.composer.holder.ParagraphHolder;
import com.haiyunshan.zima.composer.observer.SelectionObserver;

public class SelectionObservable extends Observable<SelectionObserver> {

    public void notifySelectionChanged(ParagraphHolder holder, int start, int end) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onSelectionChanged(holder, start, end);
        }
    }
}
