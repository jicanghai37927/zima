package com.haiyunshan.zima.composer.style;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import com.haiyunshan.zima.font.dataset.FontManager;

public class FontSpan extends TypefaceSpan {

    public FontSpan(String id) {
        super(id);
    }

    public String getId() {
        return super.getFamily();
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        apply(ds, getId());
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        apply(paint, getId());
    }

    private static void apply(Paint paint, String id) {
        int oldStyle;

        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        FontManager mgr = FontManager.instance();
        Typeface family = mgr.getTypeface(id);

        Typeface tf = Typeface.create(family, oldStyle);
        int fake = oldStyle & ~tf.getStyle();

        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }
}
