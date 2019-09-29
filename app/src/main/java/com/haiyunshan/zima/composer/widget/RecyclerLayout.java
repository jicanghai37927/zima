package com.haiyunshan.zima.composer.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class RecyclerLayout extends FrameLayout {

    RecyclerView mRecyclerView;
    Rect mTempRect = new Rect();

    public RecyclerLayout(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RecyclerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);

        this.ensure();
        if (mRecyclerView != null) {
            handleRecyclerView(mRecyclerView, ev);
        }

        return result;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    void handleRecyclerView(RecyclerView recyclerView, MotionEvent ev) {
        int count = recyclerView.getChildCount();
        if (count == 0) {
            return;
        }

        View view = recyclerView.getChildAt(count - 1);
        mTempRect.set(0, 0, view.getWidth(), view.getHeight());

        offsetDescendantRectToMyCoords(view, mTempRect);

        float y = ev.getY();
        int bottom = mTempRect.bottom;
        if (y > bottom) {
            view.dispatchTouchEvent(ev);
        }
    }

    void ensure() {
        if (mRecyclerView != null) {
            return;
        }

        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            if (child instanceof RecyclerView) {
                this.mRecyclerView = (RecyclerView)child;
                break;
            }
        }
    }
}
