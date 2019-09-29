package com.haiyunshan.zima.composer.style.span;

import android.text.Spannable;
import android.text.style.AlignmentSpan;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;
import com.haiyunshan.zima.composer.style.FontSpan;
import com.haiyunshan.zima.composer.style.TextSizeSpan;

public class FontItem extends BaseSpanItem<FontSpan> {

    private static final String ENTITY_TAG = "tf"; // TypeFace

    String mFont;

    public FontItem() {
        super(FontSpan.class);
    }

    public String getFont() {
        return mFont;
    }

    public void setFont(String font) {
        this.mFont = font;
    }

    @Override
    protected FontSpan create() {
        return new FontSpan(mFont);
    }

    @Override
    protected FontSpan create(FontSpan span) {
        return new FontSpan(span.getId());
    }

    @Override
    public SpanEntity toEntity(Object obj, int start, int end) {
        if (obj instanceof FontSpan) {
            FontSpan span = (FontSpan)obj;

            SpanEntity en = new SpanEntity(ENTITY_TAG, start, end);
            en.setParams(span.getId());

            return en;
        }

        return null;
    }

    @Override
    public Object toSpan(SpanEntity entity, Spannable text) {
        if (entity.getType().equalsIgnoreCase(ENTITY_TAG)) {
            FontSpan span = new FontSpan((entity.getParams()[0]));

            int start = entity.getStart();
            int end = entity.getEnd();
            int flags = this.mFlags;

            text.setSpan(span, start, end, flags);
            return span;
        }

        return null;
    }
}
