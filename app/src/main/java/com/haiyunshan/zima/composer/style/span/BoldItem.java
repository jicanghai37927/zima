package com.haiyunshan.zima.composer.style.span;

import android.text.Spannable;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;
import com.haiyunshan.zima.composer.style.BoldSpan;

public class BoldItem extends BaseSpanItem<BoldSpan> {

    private static final String ENTITY_TAG = "b"; // Bold

    public BoldItem() {
        super(BoldSpan.class);
        this.mConcat = true;
    }

    @Override
    protected BoldSpan create() {
        return new BoldSpan();
    }

    @Override
    protected BoldSpan create(BoldSpan span) {
        return new BoldSpan();
    }

    @Override
    public SpanEntity toEntity(Object obj, int start, int end) {
        if (obj instanceof BoldSpan) {
            SpanEntity en = new SpanEntity(ENTITY_TAG, start, end);
            return en;
        }

        return null;
    }

    @Override
    public Object toSpan(SpanEntity entity, Spannable text) {
        if (entity.getType().equalsIgnoreCase(ENTITY_TAG)) {
            BoldSpan span = new BoldSpan();

            int start = entity.getStart();
            int end = entity.getEnd();
            int flags = this.mFlags;

            text.setSpan(span, start, end, flags);
            return span;
        }

        return null;
    }
}
