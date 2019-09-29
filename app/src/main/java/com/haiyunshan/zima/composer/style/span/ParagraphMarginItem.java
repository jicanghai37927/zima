package com.haiyunshan.zima.composer.style.span;

import android.text.Spannable;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;
import com.haiyunshan.zima.composer.style.ParagraphMarginSpan;
import com.haiyunshan.zima.utils.Utils;

public class ParagraphMarginItem extends ParagraphSpanItem<ParagraphMarginSpan> {

    private static final String ENTITY_TAG = "pm"; // ParagraphMargin

    int mMargin;

    public ParagraphMarginItem() {
        super(ParagraphMarginSpan.class);
    }

    public int getMargin() {
        return mMargin;
    }

    public void setMargin(int margin) {
        this.mMargin = margin;
    }

    @Override
    protected ParagraphMarginSpan create() {
        return new ParagraphMarginSpan(mMargin);
    }

    @Override
    protected ParagraphMarginSpan create(ParagraphMarginSpan span) {
        return new ParagraphMarginSpan(span.getLeadingMargin(true));
    }

    @Override
    public SpanEntity toEntity(Object obj, int start, int end) {
        if (obj instanceof ParagraphMarginSpan) {
            ParagraphMarginSpan span = (ParagraphMarginSpan)obj;

            SpanEntity en = new SpanEntity(ENTITY_TAG, start, end);

            float dp = Utils.px2dp(span.getLeadingMargin(false));
            en.setParams(String.valueOf(dp));

            return en;
        }

        return null;
    }

    @Override
    public Object toSpan(SpanEntity entity, Spannable text) {
        if (entity.getType().equalsIgnoreCase(ENTITY_TAG)) {
            float dp = Float.parseFloat(entity.getParams()[0]);
            int px = (int)Utils.dp2px(dp);
            ParagraphMarginSpan span = new ParagraphMarginSpan(px);

            int start = entity.getStart();
            int end = entity.getEnd();
            int flags = this.mFlags;

            text.setSpan(span, start, end, flags);
            return span;
        }

        return null;
    }
}
