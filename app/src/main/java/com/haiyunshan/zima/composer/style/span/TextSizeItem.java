package com.haiyunshan.zima.composer.style.span;

import android.text.Spannable;
import android.text.style.StrikethroughSpan;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;
import com.haiyunshan.zima.composer.style.TextSizeSpan;

import org.w3c.dom.Text;

public class TextSizeItem extends BaseSpanItem<TextSizeSpan> {

    private static final String ENTITY_TAG = "ts"; // TextSize

    float mProportion;

    public TextSizeItem() {
        super(TextSizeSpan.class);
    }

    public float getProportion() {
        return mProportion;
    }

    public void setProportion(float proportion) {
        this.mProportion = proportion;
    }

    @Override
    protected TextSizeSpan create() {
        return new TextSizeSpan(mProportion);
    }

    @Override
    protected TextSizeSpan create(TextSizeSpan span) {
        return new TextSizeSpan(span.getProportion());
    }

    @Override
    public SpanEntity toEntity(Object obj, int start, int end) {
        if (obj instanceof TextSizeSpan) {
            TextSizeSpan span = (TextSizeSpan)obj;

            SpanEntity en = new SpanEntity(ENTITY_TAG, start, end);
            en.setParams(Float.toString(span.getProportion()));

            return en;
        }

        return null;
    }

    @Override
    public Object toSpan(SpanEntity entity, Spannable text) {
        if (entity.getType().equalsIgnoreCase(ENTITY_TAG)) {
            TextSizeSpan span = new TextSizeSpan(Float.parseFloat(entity.getParams()[0]));

            int start = entity.getStart();
            int end = entity.getEnd();
            int flags = this.mFlags;

            text.setSpan(span, start, end, flags);
            return span;
        }

        return null;
    }
}
