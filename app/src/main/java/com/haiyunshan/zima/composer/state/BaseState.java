package com.haiyunshan.zima.composer.state;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.haiyunshan.zima.composer.ComposerFragment;
import com.haiyunshan.zima.composer.BottomSheetFragment;
import com.haiyunshan.zima.composer.holder.DocumentAdapter;

public class BaseState {

    String mId;

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    DocumentAdapter mAdapter;
    BottomSheetFragment mBottomSheet;

    ComposerFragment mParent;
    Activity mContext;

    BaseState(String id, ComposerFragment parent) {
        this.mId = id;
        this.mParent = parent;
        this.mContext = parent.getActivity();

        this.mToolbar = parent.getToolbar();
        this.mRecyclerView = parent.getRecyclerView();
        this.mAdapter = parent.getAdapter();
        this.mBottomSheet = parent.getBottomSheet();
    }

    @CallSuper
    public void onEnter() {

    }

    @CallSuper
    public void onExit() {

    }

    public boolean onBackPressed() {
        return false;
    }

}
