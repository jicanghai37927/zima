package com.haiyunshan.zima.composer.style;

import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.LeadingMarginSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

public class LineMarginSpan extends LeadingMarginSpan.Standard {

    static final char[] SAMPLE = new char[] { '\u56fd' };

    TextView mView;

    int mCount;
    int mMargin;

    public LineMarginSpan() {
        super(0, 0);

        this.mCount = 2;
        this.mMargin = 0;
    }

    public void setView(TextView view) {
        this.mView = view;
    }

    public TextView getView() {
        return mView;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        if (!first) {
            return 0;
        }

        if (mView == null) {
            return mMargin;
        }

        CharSequence cs = mView.getText();
        if (!(cs instanceof Spanned)) {
            return mMargin;
        }

        Spanned text = (Spanned)cs;
        int start = text.getSpanStart(this);
        int end = text.getSpanEnd(this);
        if (start < 0 || start >= text.length()) {
            return mMargin;
        }

        float scale = 1.f;
        RelativeSizeSpan[] spans = text.getSpans(start, end, RelativeSizeSpan.class);
        for (RelativeSizeSpan span : spans) {
            if (start == 0 && end == text.length()) {
                scale *= span.getSizeChange();
            }
        }

        TextPaint paint = mView.getPaint();
        int margin = (int)(paint.measureText(SAMPLE, 0, SAMPLE.length) * scale);
        margin *= this.mCount;

        this.mMargin = margin;
        return margin;
    }
}
