package com.haiyunshan.zima.composer.holder;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.haiyunshan.zima.composer.ComposerFragment;
import com.haiyunshan.zima.composer.item.BaseItem;
import com.haiyunshan.zima.composer.item.Document;
import com.haiyunshan.zima.composer.state.BaseState;

public class BaseHolder<T extends BaseItem> extends RecyclerView.ViewHolder {

    protected int mPosition;
    protected T mItem;

    Document mDocument;
    DocumentAdapter mAdapter;
    RecyclerView mRecyclerView;
    ComposerFragment mParent;

    Handler mHandler;
    Activity mContext;

    public BaseHolder(ComposerFragment parent, View itemView) {
        super(itemView);

        this.mParent = parent;
        this.mDocument = parent.getDocument();
        this.mAdapter = parent.getAdapter();
        this.mRecyclerView = parent.getRecyclerView();

        this.mHandler = new Handler();

        this.mContext = parent.getActivity();
    }

    public ComposerFragment getParent() {
        return mParent;
    }

    public T getItem() {
        return mItem;
    }

    @CallSuper
    public void onBind(int position, T item, BaseState state) {
        this.mPosition = position;
        this.mItem = item;

    }

    public void onViewAttachedToWindow() {

    }

    public void onViewDetachedFromWindow() {
        this.save();
    }

    @CallSuper
    public void save() {

    }

    public void notifyChanged() {
        int index = mAdapter.indexOf(this.mItem);
        if (index >= 0) {
            mAdapter.notifyItemChanged(index);
        }
    }

    public boolean insertPicture(String[] array) {
        return false;
    }

    public boolean insertSeparate() {
        return false;
    }
}
