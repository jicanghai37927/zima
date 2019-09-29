package com.haiyunshan.zima.composer.style.span;

import android.text.Layout;
import android.text.Spannable;
import android.text.style.AlignmentSpan;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;

public class AlignmentItem extends ParagraphSpanItem<AlignmentSpan.Standard> {

    private static final String ENTITY_TAG = "align"; // ALIGNment

    Layout.Alignment mAlignment;

    public AlignmentItem() {
        super(AlignmentSpan.Standard.class);
    }

    public Layout.Alignment getAlignment() {
        return mAlignment;
    }

    public void setAlignment(Layout.Alignment alignment) {
        this.mAlignment = alignment;
    }

    @Override
    protected AlignmentSpan.Standard create() {
        return new AlignmentSpan.Standard(mAlignment);
    }

    @Override
    protected AlignmentSpan.Standard create(AlignmentSpan.Standard span) {
        return new AlignmentSpan.Standard(span.getAlignment());
    }

    @Override
    public SpanEntity toEntity(Object obj, int start, int end) {
        if (obj instanceof AlignmentSpan.Standard) {
            AlignmentSpan.Standard span = (AlignmentSpan.Standard)obj;

            SpanEntity en = new SpanEntity(ENTITY_TAG, start, end);
            en.setParams(fromAlignment(span.getAlignment()));

            return en;
        }

        return null;
    }

    @Override
    public Object toSpan(SpanEntity entity, Spannable text) {
        if (entity.getType().equalsIgnoreCase(ENTITY_TAG)) {
            AlignmentSpan.Standard span = new AlignmentSpan.Standard(toAlignment(entity.getParams()[0]));

            int start = entity.getStart();
            int end = entity.getEnd();
            int flags = this.mFlags;

            text.setSpan(span, start, end, flags);
            return span;
        }

        return null;
    }

    String fromAlignment(Layout.Alignment align) {
        String text = "normal";
        if (align == Layout.Alignment.ALIGN_CENTER) {
            text = "center";
        } else if (align == Layout.Alignment.ALIGN_OPPOSITE) {
            text = "opposite";
        }

        return text;
    }

    Layout.Alignment toAlignment(String text) {
        Layout.Alignment align = Layout.Alignment.ALIGN_NORMAL;
        if (text.equalsIgnoreCase("center")) {
            align = Layout.Alignment.ALIGN_CENTER;
        } else if (text.equalsIgnoreCase("opposite")) {
            align = Layout.Alignment.ALIGN_OPPOSITE;
        }

        return align;
    }
}
