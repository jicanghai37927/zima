package com.haiyunshan.zima.composer.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.TabStopSpan;
import android.text.style.UpdateAppearance;
import android.util.Log;

import com.haiyunshan.zima.R;
import com.haiyunshan.zima.ZimaApp;

public class HighlightSpan implements UpdateAppearance {

    String mColor;
    Drawable mDrawable;

    Rect mRect;

    public HighlightSpan(String color) {
        this.mColor = color;
        this.mDrawable = ZimaApp.instance().getDrawable(R.drawable.bdreader_note_bg_brown_1);

        this.mRect = new Rect();
    }

    public HighlightSpan(HighlightSpan span) {
        this.mColor = span.mColor;
        this.mDrawable = span.mDrawable;

        this.mRect = new Rect();
    }

    public String getColor() {
        return mColor;
    }

    public void draw(Layout layout, Canvas c, TextPaint p,
                     int top, int baseline, int bottom,
                     Spanned text, int start, int end, int lnum) {

        if (layout == null) {
            return;
        }

        if (isEmptyLine(text, start, end)) {
            return;
        }

        int spanStart = text.getSpanStart(this);
        int spanEnd = text.getSpanEnd(this);

        if (start < spanStart) {
            start = spanStart;
        }

        if (end > spanEnd) {
            end = spanEnd;
        } else {
            if ((end > 0 && end < text.length()) && text.charAt(end - 1) == '\n') {
                end -= 1;
            }
        }

        Rect r = this.mRect;

        int trick = (int)((baseline - top) * 0.16f);

        r.top = top;
        r.bottom = baseline + trick;
        r.left = (int) layout.getPrimaryHorizontal(start);
        r.right = (int) layout.getSecondaryHorizontal(end);
        if (r.right == 0) {
//            r.right = (int)layout.getLineWidth(lnum);
//            r.right = (int)layout.getLineMax(lnum);
            r.right = (int)layout.getLineRight(lnum);
        }

        mDrawable.setBounds(r);
        mDrawable.draw(c);
    }

    boolean isEmptyLine(Spanned text, int start, int end) {
        boolean result = true;

        for (int i = start; i < end; i++) {
            char c = text.charAt(i);
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                continue;
            }

            result = false;
            break;
        }

        return result;
    }


}
