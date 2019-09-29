package com.haiyunshan.zima.composer.style.span;

import android.text.Spannable;
import android.widget.TextView;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;
import com.haiyunshan.zima.composer.style.LineMarginSpan;

public class LineMarginItem extends ParagraphSpanItem<LineMarginSpan> {

    private static final String ENTITY_TAG = "lm"; // LineMargin

    TextView mView;

    public LineMarginItem() {
        super(LineMarginSpan.class);
    }

    public void setView(TextView view) {
        this.mView = view;
    }

    @Override
    protected LineMarginSpan create() {
        LineMarginSpan span = new LineMarginSpan();
        span.setView(mView);

        return span;
    }

    @Override
    protected LineMarginSpan create(LineMarginSpan span) {
        LineMarginSpan target = new LineMarginSpan();
        target.setView(span.getView());

        return target;
    }

    @Override
    public SpanEntity toEntity(Object obj, int start, int end) {
        if (obj instanceof LineMarginSpan) {
            SpanEntity en = new SpanEntity(ENTITY_TAG, start, end);
            return en;
        }

        return null;
    }

    @Override
    public Object toSpan(SpanEntity entity, Spannable text) {
        if (entity.getType().equalsIgnoreCase(ENTITY_TAG)) {
            LineMarginSpan span = new LineMarginSpan();

            int start = entity.getStart();
            int end = entity.getEnd();
            int flags = this.mFlags;

            text.setSpan(span, start, end, flags);
            return span;
        }

        return null;
    }
}
