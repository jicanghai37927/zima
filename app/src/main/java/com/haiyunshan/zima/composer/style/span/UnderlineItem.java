package com.haiyunshan.zima.composer.style.span;

import android.text.Spannable;
import android.text.style.UnderlineSpan;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;

public class UnderlineItem extends BaseSpanItem<UnderlineSpan> {

    private static final String ENTITY_TAG = "u"; // Underline

    public UnderlineItem() {
        super(UnderlineSpan.class);
        this.mConcat = true;
    }

    @Override
    protected UnderlineSpan create() {
        return new UnderlineSpan();
    }

    @Override
    protected UnderlineSpan create(UnderlineSpan span) {
        return new UnderlineSpan();
    }

    @Override
    public SpanEntity toEntity(Object obj, int start, int end) {
        if (obj instanceof UnderlineSpan) {
            SpanEntity en = new SpanEntity(ENTITY_TAG, start, end);
            return en;
        }

        return null;
    }

    @Override
    public Object toSpan(SpanEntity entity, Spannable text) {
        if (entity.getType().equalsIgnoreCase(ENTITY_TAG)) {
            UnderlineSpan span = new UnderlineSpan();

            int start = entity.getStart();
            int end = entity.getEnd();
            int flags = this.mFlags;

            text.setSpan(span, start, end, flags);
            return span;
        }

        return null;
    }
}
