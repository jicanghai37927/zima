package com.haiyunshan.zima.composer.style.span;

import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;
import com.haiyunshan.zima.composer.style.HighlightSpan;
import com.haiyunshan.zima.utils.Utils;

public class HighlightItem extends BaseSpanItem<HighlightSpan> {

    private static final String ENTITY_TAG = "hlp"; // HighLightPoint

    String mColor;

    public HighlightItem() {
        super(HighlightSpan.class);
        this.mConcat = true;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        this.mColor = color;
    }

    @Override
    protected boolean accept(HighlightSpan span) {
        if (TextUtils.isEmpty(mColor)) {
            return true;
        }

        return span.getColor().equalsIgnoreCase(mColor);
    }

    @Override
    protected HighlightSpan create() {
        return new HighlightSpan(this.mColor);
    }

    @Override
    protected HighlightSpan create(HighlightSpan span) {
        return new HighlightSpan(span);
    }

    @Override
    public SpanEntity toEntity(Object obj, int start, int end) {
        if (obj instanceof HighlightSpan) {
            HighlightSpan span = (HighlightSpan)obj;

            SpanEntity en = new SpanEntity(ENTITY_TAG, start, end);
            en.setParams(span.getColor());

            return en;
        }

        return null;
    }

    @Override
    public Object toSpan(SpanEntity entity, Spannable text) {
        if (entity.getType().equalsIgnoreCase(ENTITY_TAG)) {
            HighlightSpan span = new HighlightSpan((entity.getParams()[0]));

            int start = entity.getStart();
            int end = entity.getEnd();
            int flags = this.mFlags;

            text.setSpan(span, start, end, flags);
            return span;
        }

        return null;
    }
}
