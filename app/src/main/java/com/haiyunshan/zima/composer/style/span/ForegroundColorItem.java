package com.haiyunshan.zima.composer.style.span;

import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;
import com.haiyunshan.zima.composer.style.FontSpan;
import com.haiyunshan.zima.utils.Utils;

public class ForegroundColorItem extends BaseSpanItem<ForegroundColorSpan> {

    private static final String ENTITY_TAG = "fgc"; // ForeGroundColor

    int mColor;

    public ForegroundColorItem() {
        super(ForegroundColorSpan.class);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    @Override
    protected ForegroundColorSpan create() {
        return new ForegroundColorSpan(mColor);
    }

    @Override
    protected ForegroundColorSpan create(ForegroundColorSpan span) {
        return new ForegroundColorSpan(span.getForegroundColor());
    }

    @Override
    public SpanEntity toEntity(Object obj, int start, int end) {
        if (obj instanceof ForegroundColorSpan) {
            ForegroundColorSpan span = (ForegroundColorSpan)obj;

            SpanEntity en = new SpanEntity(ENTITY_TAG, start, end);
            en.setParams(Utils.fromColor(span.getForegroundColor()));

            return en;
        }

        return null;
    }

    @Override
    public Object toSpan(SpanEntity entity, Spannable text) {
        if (entity.getType().equalsIgnoreCase(ENTITY_TAG)) {
            ForegroundColorSpan span = new ForegroundColorSpan(Utils.parseColor(entity.getParams()[0]));

            int start = entity.getStart();
            int end = entity.getEnd();
            int flags = this.mFlags;

            text.setSpan(span, start, end, flags);
            return span;
        }

        return null;
    }
}
