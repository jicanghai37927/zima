package com.haiyunshan.zima.composer.style.span;

import android.text.Spannable;
import android.text.style.StrikethroughSpan;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;
import com.haiyunshan.zima.composer.style.ItalicSpan;

public class StrikethroughItem extends BaseSpanItem<StrikethroughSpan> {

    private static final String ENTITY_TAG = "s"; // Strikethrough

    public StrikethroughItem() {
        super(StrikethroughSpan.class);
        this.mConcat = true;
    }

    @Override
    protected StrikethroughSpan create() {
        return new StrikethroughSpan();
    }

    @Override
    protected StrikethroughSpan create(StrikethroughSpan span) {
        return new StrikethroughSpan();
    }

    @Override
    public SpanEntity toEntity(Object obj, int start, int end) {
        if (obj instanceof StrikethroughSpan) {
            SpanEntity en = new SpanEntity(ENTITY_TAG, start, end);
            return en;
        }

        return null;
    }

    @Override
    public Object toSpan(SpanEntity entity, Spannable text) {
        if (entity.getType().equalsIgnoreCase(ENTITY_TAG)) {
            StrikethroughSpan span = new StrikethroughSpan();

            int start = entity.getStart();
            int end = entity.getEnd();
            int flags = this.mFlags;

            text.setSpan(span, start, end, flags);
            return span;
        }

        return null;
    }
}
