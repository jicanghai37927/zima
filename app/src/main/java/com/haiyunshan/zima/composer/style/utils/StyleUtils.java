package com.haiyunshan.zima.composer.style.utils;

import android.text.Spannable;
import android.text.Spanned;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;
import com.haiyunshan.zima.composer.style.span.BaseSpanItem;

import java.util.List;

public class StyleUtils {

    static final BaseSpanItem[] SPANS = new BaseSpanItem[] {

            // 字符
            Spans.BOLD_ITEM,
            Spans.ITALIC_ITEM,
            Spans.UNDERLINE_ITEM,
            Spans.STRIKETHROUGH_ITEM,
            Spans.HIGHLIGHT_ITEM,
            Spans.TEXT_SIZE_ITEM,
            Spans.FONT_ITEM,
            Spans.FG_COLOR_ITEM,

            // 段落
            Spans.ALIGNMENT_ITEM,
            Spans.LINE_MARGIN_ITEM,
            Spans.MARGIN_ITEM,
    };

    public static final void getSpans(List<SpanEntity> list, Spanned text) {
        BaseSpanItem[] array = SPANS;

        Object[] spans = text.getSpans(0, text.length(), Object.class);
        for (Object obj : spans) {
            int start = text.getSpanStart(obj);
            int end = text.getSpanEnd(obj);

            for (BaseSpanItem item : array) {
                SpanEntity en = item.toEntity(obj, start, end);
                if (en != null) {

                    list.add(en);

                    break;
                }
            }
        }
    }

    public static final void toSpans(List<SpanEntity> list, Spannable text) {
        BaseSpanItem[] array = SPANS;

        for (SpanEntity entity : list) {
            for (BaseSpanItem item : array) {
                Object obj = item.toSpan(entity, text);
                if (obj != null) {
                    break;
                }
            }
        }
    }
}
