package com.haiyunshan.zima.composer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.haiyunshan.zima.composer.style.utils.HighlightPainter;

import java.lang.reflect.Method;

public class ParagraphEditText extends AppCompatEditText {

    HighlightPainter mHighlightPainter;

    OnSelectionChangeListener mOnSelectionChangeListener;

    public ParagraphEditText(Context context) {
        this(context, null);
    }

    public ParagraphEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public ParagraphEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.getLayout() == null) {
            assumeLayoutByReflect();
        }

        if (getText() instanceof Spanned) {

            if (mHighlightPainter == null) {
                mHighlightPainter = new HighlightPainter(this);
            }

            mHighlightPainter.draw(canvas);
        }

        super.onDraw(canvas);

    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        if (mOnSelectionChangeListener != null) {
            mOnSelectionChangeListener.onSelectionChanged(this, selStart, selEnd);
        }
    }

    public void setOnSelectionChangeListener(OnSelectionChangeListener l) {
        this.mOnSelectionChangeListener = l;
    }

    private void assumeLayoutByReflect() {
        try {
            Method method = TextView.class.getDeclaredMethod("assumeLayout");
            method.setAccessible(true);
            method.invoke(this);

            Log.w("AA", "assumeLayoutByReflect");
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("AA", "assumeLayoutByReflect fail");
        }
    }

    public interface OnSelectionChangeListener {

        void onSelectionChanged(ParagraphEditText view, int start, int end);

    }
}
