package com.haiyunshan.zima.composer.style.span;

import android.text.Spannable;
import android.text.style.LeadingMarginSpan;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;
import com.haiyunshan.zima.composer.style.ItalicSpan;
import com.haiyunshan.zima.utils.Utils;

public class ItalicItem extends BaseSpanItem<ItalicSpan> {

    private static final String ENTITY_TAG = "i"; // Italic

    public ItalicItem() {
        super(ItalicSpan.class);
        this.mConcat = true;
    }

    @Override
    protected ItalicSpan create() {
        return new ItalicSpan();
    }

    @Override
    protected ItalicSpan create(ItalicSpan span) {
        return new ItalicSpan();
    }

    @Override
    public SpanEntity toEntity(Object obj, int start, int end) {
        if (obj instanceof ItalicSpan) {
            SpanEntity en = new SpanEntity(ENTITY_TAG, start, end);
            return en;
        }

        return null;
    }

    @Override
    public Object toSpan(SpanEntity entity, Spannable text) {
        if (entity.getType().equalsIgnoreCase(ENTITY_TAG)) {
            ItalicSpan span = new ItalicSpan();

            int start = entity.getStart();
            int end = entity.getEnd();
            int flags = this.mFlags;

            text.setSpan(span, start, end, flags);
            return span;
        }

        return null;
    }
}
