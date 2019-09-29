package com.haiyunshan.zima.composer.style.utils;

import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.widget.EditText;

import com.haiyunshan.zima.composer.style.FontSpan;
import com.haiyunshan.zima.composer.style.LineMarginSpan;
import com.haiyunshan.zima.composer.style.TextSizeSpan;
import com.haiyunshan.zima.composer.style.span.AlignmentItem;
import com.haiyunshan.zima.composer.style.span.BaseSpanItem;
import com.haiyunshan.zima.composer.style.span.FontItem;
import com.haiyunshan.zima.composer.style.span.ForegroundColorItem;
import com.haiyunshan.zima.composer.style.span.HighlightItem;
import com.haiyunshan.zima.composer.style.span.LineMarginItem;
import com.haiyunshan.zima.composer.style.span.ParagraphMarginItem;
import com.haiyunshan.zima.composer.style.span.TextSizeItem;

public class SpanUtils {

    public static final void attach(EditText view) {
        Editable text = view.getText();
        LineMarginSpan[] spans = text.getSpans(0, text.length(), LineMarginSpan.class);
        for (LineMarginSpan span : spans) {
            span.setView(view);
        }
    }

    public static final void detach(CharSequence cs) {
        if (!(cs instanceof Spannable)) {
            return;
        }

        Spannable text = (Spannable)cs;
        LineMarginSpan[] spans = text.getSpans(0, text.length(), LineMarginSpan.class);
        for (LineMarginSpan span : spans) {
            span.setView(null);
        }
    }

    public static final boolean isHighlight(EditText view) {
        Editable text = view.getText();
        HighlightItem item = Spans.HIGHLIGHT_ITEM;
        item.setColor("");

        return item.exist(text);
    }

    public static final boolean isHighlight(EditText view, String color) {
        Editable text = view.getText();
        HighlightItem item = Spans.HIGHLIGHT_ITEM;
        item.setColor(color);


        return item.exist(text);
    }

    public static final boolean toggleHighlight(EditText view, String color) {
        Editable text = view.getText();
        HighlightItem item = Spans.HIGHLIGHT_ITEM;
        item.setColor(color);


        return item.toggle(text);
    }

    public static final boolean isBold(EditText view) {
        Editable text = view.getText();
        BaseSpanItem item = Spans.BOLD_ITEM;


        return item.exist(text);
    }

    public static final boolean toggleBold(EditText view) {
        Editable text = view.getText();
        BaseSpanItem item = Spans.BOLD_ITEM;


        return item.toggle(text);
    }

    public static final boolean isItalic(EditText view) {
        Editable text = view.getText();
        BaseSpanItem item = Spans.ITALIC_ITEM;


        return item.exist(text);
    }

    public static final boolean toggleItalic(EditText view) {
        Editable text = view.getText();
        BaseSpanItem item = Spans.ITALIC_ITEM;


        return item.toggle(text);
    }


    public static final boolean isUnderline(EditText view) {
        Editable text = view.getText();
        BaseSpanItem item = Spans.UNDERLINE_ITEM;


        return item.exist(text);
    }

    public static final boolean toggleUnderline(EditText view) {
        Editable text = view.getText();
        BaseSpanItem item = Spans.UNDERLINE_ITEM;


        return item.toggle(text);
    }


    public static final boolean isStrikethrough(EditText view) {
        Editable text = view.getText();
        BaseSpanItem item = Spans.STRIKETHROUGH_ITEM;


        return item.exist(text);
    }

    public static final boolean toggleStrikethrough(EditText view) {
        Editable text = view.getText();
        BaseSpanItem item = Spans.STRIKETHROUGH_ITEM;


        return item.toggle(text);
    }

    public static final Layout.Alignment getAlignment(EditText view) {
        Editable text = view.getText();

        AlignmentItem item = Spans.ALIGNMENT_ITEM;
        item.setAlignment(null);

        AlignmentSpan span = item.peek(text);
        if (span == null) {
            return null;
        }

        return span.getAlignment();
    }

    public static final void setAlignment(EditText view, Layout.Alignment align) {
        Editable text = view.getText();

        AlignmentItem item = Spans.ALIGNMENT_ITEM;
        item.setAlignment(align);

        Layout.Alignment source = view.getLayout().getAlignment();
        if (source == align) { // 与View的设置一致，清除即可
            item.clear(text);
        } else {
            item.clear(text); // 清除所有样式，然后设置
            item.set(text);
        }
    }

    public static final float getTextSize(EditText view) {
        Editable text = view.getText();

        float size = view.getTextSize(); // px
        float density = view.getPaint().density;
        size = size / density; // sp

        TextSizeItem item = Spans.TEXT_SIZE_ITEM;
        TextSizeSpan span = item.peek(text);
        if (span == null) {
            return size;
        }

        size *= span.getProportion();
        return size;
    }

    public static final void setTextSize(EditText view, float textSize) {
        Editable text = view.getText();

        float size = view.getTextSize(); // px
        float density = view.getPaint().density;
        size = size / density; // sp

        TextSizeItem item = Spans.TEXT_SIZE_ITEM;
        item.setProportion(textSize / size);

        if (Math.abs(textSize - size) / size < 0.01f) {
            item.clear(text);
        } else {
            item.clear(text);
            item.set(text);
        }
    }

    public static final String getFont(EditText view) {
        Editable text = view.getText();

        FontItem item = Spans.FONT_ITEM;
        FontSpan span = item.peek(text);
        if (span == null) {
            return "";
        }

        return span.getId();
    }

    public static final void setFont(EditText view, String font) {
        Editable text = view.getText();

        FontItem item = Spans.FONT_ITEM;
        item.setFont(font);

        if (TextUtils.isEmpty(font)) {
            item.clear(text);
        } else {
            item.clear(text);
            item.set(text);
        }
    }

    public static final int getForegroundColor(EditText view) {
        Editable text = view.getText();

        ForegroundColorItem item = Spans.FG_COLOR_ITEM;
        ForegroundColorSpan span = item.peek(text);
        if (span == null) {
            return 0;
        }

        int value = span.getForegroundColor();
        return value;
    }

    public static final void setForegroundColor(EditText view, int color) {
        Editable text = view.getText();

        ForegroundColorItem item = Spans.FG_COLOR_ITEM;
        item.setColor(color);

        if (color == 0) {
            item.clear(text);
        } else {
            item.clear(text);
            item.set(text);
        }
    }

    public static final boolean isLineMargin(EditText view) {
        Editable text = view.getText();
        LineMarginItem item = Spans.LINE_MARGIN_ITEM;

        return item.exist(text);
    }

    public static final boolean toggleLineMargin(EditText view) {
        Editable text = view.getText();
        LineMarginItem item = Spans.LINE_MARGIN_ITEM;
        item.setView(view);

        boolean exist = item.exist(text);
        if (exist) {
            item.clear(text);
        } else {
            item.clear(text);
            item.set(text);
        }

        item.setView(null);
        return !exist;
    }


    public static final int getIndent(EditText view) {
        Editable text = view.getText();

        ParagraphMarginItem item = Spans.MARGIN_ITEM;
        LeadingMarginSpan span = item.peek(text);
        if (span == null) {
            return 0;
        }

        int indent = span.getLeadingMargin(false);
        return indent;
    }

    public static final void setIndent(EditText view, int indent) {
        Editable text = view.getText();

        ParagraphMarginItem item = Spans.MARGIN_ITEM;
        item.setMargin(indent);

        if (indent <= 0) {
            item.clear(text);
        } else {
            item.clear(text);
            item.set(text);
        }
    }
}
